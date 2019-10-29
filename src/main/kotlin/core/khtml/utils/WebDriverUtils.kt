package core.khtml.utils

import core.khtml.annotations.Wait
import core.khtml.build.XpathBuilder.Companion.buildXpath
import core.khtml.conf.FullXpath
import core.khtml.waits.WaitCondition
import core.khtml.waits.WaitElement
import org.openqa.selenium.By
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.TimeUnit

object WebDriverUtils {
    private const val timeWait: Long = 30
    private const val sleepFor = 500L

    fun safeOperation(block: () -> Any?): Any? {
        val end = laterBy(TimeUnit.SECONDS.toMillis(timeWait))
        var lastException: StaleElementReferenceException
        do {
            try {
                return block()
            } catch (e: StaleElementReferenceException) {
                lastException = e
                waitFor()
            }
        } while (isNowBefore(end))
        throw lastException
    }

    fun execWebElementAction(
            xpath: String,
            driver: WebDriver,
            block: (element: WebElement) -> Any?
    ): Any? {
        return safeOperation {
            block(driver.findElement(By.xpath(xpath)))!!
        }
    }

    private fun waitFor() {
        Thread.sleep(sleepFor)
    }

    private fun laterBy(durationInMillis: Long): Long {
        return System.currentTimeMillis() + durationInMillis
    }

    private fun isNowBefore(endInMillis: Long): Boolean {
        return System.currentTimeMillis() < endInMillis
    }

    fun waitConditionFragment(method: Method, driver: WebDriver, fullXpath: LinkedList<FullXpath>) {
        val conditionAnnotation = method.getAnnotation(Wait::class.java).condition
        val pollingAnnotation = method.getAnnotation(Wait::class.java).polling
        val timeAnnotation = method.getAnnotation(Wait::class.java).time
        val waitElement = WaitElement(driver, xpath = buildXpath(fullXpath))
        when (conditionAnnotation) {
            WaitCondition.TO_CLICKABLE -> waitElement.waitClickable(timeAnnotation, pollingAnnotation)
            WaitCondition.ENABLED -> waitElement.waitEnabled(timeAnnotation, pollingAnnotation)
            WaitCondition.DISABLED -> waitElement.waitDisabled(timeAnnotation, pollingAnnotation)
            WaitCondition.AJAX -> waitElement.waitAjax(timeAnnotation, pollingAnnotation)
            WaitCondition.DISPLAY -> waitElement.waitDisabled(timeAnnotation, pollingAnnotation)
            WaitCondition.TEXT_PRESENT -> waitElement.waitTextPresent(timeAnnotation, pollingAnnotation)
            WaitCondition.INVISIBLE -> waitElement.waitInvisible(timeAnnotation, pollingAnnotation)
            WaitCondition.NOT_EXITS -> waitElement.waitNotExists(timeAnnotation, pollingAnnotation)
            WaitCondition.ALL_AJAX -> waitElement.waitAllAjax(timeAnnotation, pollingAnnotation)
        }
    }
}

