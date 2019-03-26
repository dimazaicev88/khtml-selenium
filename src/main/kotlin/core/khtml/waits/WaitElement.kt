package core.khtml.waits

import core.khtml.ext.js
import org.openqa.selenium.By
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.FluentWait
import java.time.Duration


class WaitElement constructor(
    private val xpath: String,
    private val driver: WebDriver,
    private val timeOutInSeconds: Long = 30,
    private val polling: Long = 500
) {

    private val fluentWait: FluentWait<WebDriver>
        get() {
            val duration = Duration.ofSeconds(timeOutInSeconds)
            return FluentWait(driver)
                .withTimeout(duration)
                .pollingEvery(Duration.ofMillis(polling))
                .ignoreAll(
                    listOf(
                        org.openqa.selenium.NoSuchElementException::class.java,
                        StaleElementReferenceException::class.java
                    )
                )
        }

    fun waitCondition(condition: WaitCondition) {
        when (condition) {
            WaitCondition.TO_CLICKABLE -> waitClickable()
            WaitCondition.ENABLED -> waitEnabled()
            WaitCondition.DISABLED -> waitDisabled()
            WaitCondition.AJAX -> waitAjax()
            WaitCondition.DISPLAY -> waitDisplay()
            WaitCondition.TEXT_PRESENT -> waitTextPresent()
            WaitCondition.INVISIBLE -> waitInvisible()
        }
    }

    fun waitCustomCondition(conditionValue: Any) {
        fluentWait.until { conditionValue }
    }

    private fun waitAjax() {
        waitCustomCondition {
            driver.js(
                "if(window.jQuery!=null){\n" +
                        "  return false\n" +
                        "}else{\n" +
                        " return window.jQuery.active == 0\n" +
                        "}"
            ).toString().toBoolean()
        }
    }

    private fun waitTextPresent() {
        fluentWait.until { driver.findElement(By.xpath(xpath)).text.isNotEmpty() }
    }

    private fun waitDisplay() {
        fluentWait.until { driver.findElement(By.xpath(xpath)).isDisplayed }
    }

    private fun waitClickable() {
        fluentWait.until {
            val element = driver.findElement(By.xpath(xpath))
            element.isDisplayed && element.isEnabled
        }
    }

    private fun waitInvisible() {
        fluentWait.until { !driver.findElement(By.xpath(xpath)).isDisplayed }
    }

    private fun waitEnabled() {
        fluentWait.until { driver.findElement(By.xpath(xpath)).isEnabled }
    }

    private fun waitDisabled() {
        fluentWait.until { !driver.findElement(By.xpath(xpath)).isEnabled }
    }
}