package core.khtml.waits


import core.khtml.ext.js
import core.khtml.ext.jsFindElement
import org.openqa.selenium.By
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.FluentWait
import java.time.Duration


class WaitElement constructor(private val driver: WebDriver, var xpath: String = "") {

    private fun defaultFluentWait(timeOut: Long, polling: Long, message: String = ""): FluentWait<WebDriver> {
        return FluentWait(driver)
                .withTimeout(Duration.ofSeconds(timeOut))
                .pollingEvery(Duration.ofMillis(polling))
                .withMessage(message)
                .ignoreAll(
                        listOf(
                                org.openqa.selenium.NoSuchElementException::class.java,
                                StaleElementReferenceException::class.java
                        )
                )
    }

    private fun getFluentWait(timeOut: Long, polling: Long, fw: FluentWait<WebDriver>? = null): FluentWait<WebDriver> {
        return fw ?: defaultFluentWait(timeOut, polling)
    }

    fun sleep(time: Long = 400): WaitElement {
        Thread.sleep(time)
        return this
    }

    fun waitCustomCondition(timeOut: Long = 5, polling: Long = 100, fw: FluentWait<WebDriver>? = null, condition: () -> Boolean): WaitElement {
        getFluentWait(timeOut, polling, fw).until { condition() }
        return this
    }

    fun waitAjax(timeOut: Long = 300, polling: Long = 30, fw: FluentWait<WebDriver>? = null): WaitElement {
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

    fun waitTextPresent(timeOut: Long, polling: Long, fw: FluentWait<WebDriver>? = null): WaitElement {
        getFluentWait(timeOut, polling, fw).until { driver.findElement(By.xpath(xpath)).text.isNotEmpty() }
        return this
    }

    fun waitDisplay(timeOut: Long, polling: Long, fw: FluentWait<WebDriver>? = null): WaitElement {
        getFluentWait(timeOut, polling, fw).until { driver.findElement(By.xpath(xpath)).isDisplayed }
        return this
    }

    fun waitClickable(timeOut: Long, polling: Long, fw: FluentWait<WebDriver>? = null): WaitElement {
        getFluentWait(timeOut, polling, fw).until {
            val element = driver.findElement(By.xpath(xpath))
            element.isDisplayed && element.isEnabled
        }
        return this
    }

    fun waitInvisible(timeOut: Long, polling: Long, fw: FluentWait<WebDriver>? = null): WaitElement {
        getFluentWait(timeOut, polling, fw).until { !driver.findElement(By.xpath(xpath)).isDisplayed }
        return this
    }

    fun waitNotExists(timeOut: Long, polling: Long, fw: FluentWait<WebDriver>? = null): WaitElement {
        getFluentWait(timeOut, polling, fw).until {
            driver.js("return ${driver.jsFindElement(xpath)}.singleNodeValue==null").toString().toBoolean()
        }
        return this
    }

    fun waitEnabled(timeOut: Long, polling: Long, fw: FluentWait<WebDriver>? = null): WaitElement {
        getFluentWait(timeOut, polling, fw).until { driver.findElement(By.xpath(xpath)).isEnabled }
        return this
    }

    fun waitDisabled(timeOut: Long, polling: Long, fw: FluentWait<WebDriver>? = null): WaitElement {
        getFluentWait(timeOut, polling, fw).until { !driver.findElement(By.xpath(xpath)).isEnabled }
        return this
    }

    @Suppress("ThrowableNotThrown")
    fun waitAllAjax(timeOut: Long, polling: Long, fw: FluentWait<WebDriver>? = null): WaitElement {
        Thread.sleep(500)
        if (driver.js("return window.openHTTPs === undefined").toString().toBoolean()) {
            RuntimeException("AJAX Interceptor not register")
        }
        waitCustomCondition(timeOut, polling, fw) {
            driver.js("return window.openHTTPs === 0").toString().toBoolean()
        }
        return this
    }

    companion object {

        fun addInterceptorAJAX(driver: WebDriver) {
            driver.js("if (window.openHTTPs === undefined) {\n" +
                    "    (function () {\n" +
                    "        var oldOpen = XMLHttpRequest.prototype.open;\n" +
                    "        window.openHTTPs = 0;\n" +
                    "        XMLHttpRequest.prototype.open = function (method, url, async, user, pass) {\n" +
                    "            console.log(\"Url: \" + url);\n" +
                    "            window.openHTTPs++;\n" +
                    "            this.addEventListener(\"readystatechange\", function () {\n" +
                    "                if (this.readyState === 4) {\n" +
                    "                    window.openHTTPs--;\n" +
                    "                }\n" +
                    "            }, false);\n" +
                    "            oldOpen.call(this, method, url, async, user, pass);\n" +
                    "        }\n" +
                    "    })(XMLHttpRequest);\n" +
                    "}")
        }
    }
}