package org.intsite.khtml.waits


import org.intsite.khtml.ext.js
import org.intsite.khtml.ext.jsFindElement
import org.openqa.selenium.By
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.FluentWait
import java.time.Duration


class WaitElement constructor(private val driver: WebDriver, var xpath: String = "") {

    private fun defaultFluentWait(timeOut: Duration, polling: Duration, message: String = ""): FluentWait<WebDriver> {
        return FluentWait(driver)
            .withTimeout(timeOut)
            .pollingEvery(polling)
            .withMessage(message)
            .ignoreAll(
                listOf(
                    org.openqa.selenium.NoSuchElementException::class.java,
                    StaleElementReferenceException::class.java
                )
            )
    }

    private fun getFluentWait(
        timeOut: Duration,
        polling: Duration,
        fw: FluentWait<WebDriver>? = null
    ): FluentWait<WebDriver> {
        return fw ?: defaultFluentWait(timeOut, polling)
    }

    fun sleep(time: Long = 400): WaitElement {
        Thread.sleep(time)
        return this
    }

    fun waitCustomCondition(
        timeOut: Duration = Duration.ofSeconds(15),
        polling: Duration = Duration.ofMillis(500),
        fw: FluentWait<WebDriver>? = null,
        condition: () -> Boolean
    ): WaitElement {
        getFluentWait(timeOut, polling, fw).until { condition() }
        return this
    }

    fun waitJqueryXHR(
        timeOut: Duration = Duration.ofSeconds(30),
        polling: Duration = Duration.ofMillis(500),
        fw: FluentWait<WebDriver>? = null
    ): WaitElement {
        waitCustomCondition(timeOut, polling, fw) {
            driver.js(
                "if(window.jQuery==null){\n" +
                        "  return true\n" +
                        "}else{\n" +
                        " return window.jQuery.active == 0\n" +
                        "}"
            ).toString().toBoolean()
        }
        return this
    }

    fun waitTextPresent(timeOut: Duration, polling: Duration, fw: FluentWait<WebDriver>? = null): WaitElement {
        getFluentWait(timeOut, polling, fw).until { driver.findElement(By.xpath(xpath)).text.isNotEmpty() }
        return this
    }

    fun waitDisplay(timeOut: Duration, polling: Duration, fw: FluentWait<WebDriver>? = null): WaitElement {
        getFluentWait(timeOut, polling, fw).until { driver.findElement(By.xpath(xpath)).isDisplayed }
        return this
    }

    fun waitClickable(timeOut: Duration, polling: Duration, fw: FluentWait<WebDriver>? = null): WaitElement {
        getFluentWait(timeOut, polling, fw).until {
            val element = driver.findElement(By.xpath(xpath))
            element.isDisplayed && element.isEnabled
        }
        return this
    }

    fun waitInvisible(timeOut: Duration, polling: Duration, fw: FluentWait<WebDriver>? = null): WaitElement {
        getFluentWait(timeOut, polling, fw).until { !driver.findElement(By.xpath(xpath)).isDisplayed }
        return this
    }

    fun waitNotExists(timeOut: Duration, polling: Duration, fw: FluentWait<WebDriver>? = null): WaitElement {
        getFluentWait(timeOut, polling, fw).until {
            driver.js("return ${driver.jsFindElement(xpath)}.singleNodeValue==null").toString().toBoolean()
        }
        return this
    }

    fun waitEnabled(timeOut: Duration, polling: Duration, fw: FluentWait<WebDriver>? = null): WaitElement {
        getFluentWait(timeOut, polling, fw).until { driver.findElement(By.xpath(xpath)).isEnabled }
        return this
    }

    fun waitDisabled(timeOut: Duration, polling: Duration, fw: FluentWait<WebDriver>? = null): WaitElement {
        getFluentWait(timeOut, polling, fw).until { !driver.findElement(By.xpath(xpath)).isEnabled }
        return this
    }

    fun waitExists(timeOut: Duration, polling: Duration, fw: FluentWait<WebDriver>? = null): WaitElement {
        getFluentWait(timeOut, polling, fw).until {
            driver.js("return ${driver.jsFindElement(xpath)}.singleNodeValue!=null").toString().toBoolean()
        }
        return this
    }

    @Suppress("ThrowableNotThrown")
    fun waitAllAjax(timeOut: Duration, polling: Duration, fw: FluentWait<WebDriver>? = null): WaitElement {
        if (driver.js("return window.openHTTPs === undefined").toString().toBoolean()) {
            RuntimeException("AJAX Interceptor not register")
        }
        waitCustomCondition(timeOut, polling, fw) {
            driver.js("return window.openHTTPs === 0").toString().toBoolean()
        }
        return this
    }
}