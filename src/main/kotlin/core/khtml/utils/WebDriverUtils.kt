package core.khtml.utils

import core.khtml.annotations.Wait
import core.khtml.build.XpathBuilder
import core.khtml.conf.FullXpath
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

    fun searchWebElement(driver: WebDriver, xpath: String, searchType: SearchType): Any {
        val end = laterBy(TimeUnit.SECONDS.toMillis(timeWait))
        var lastException: StaleElementReferenceException
        do {
            try {
                return when (searchType) {
                    SearchType.SINGLE -> driver.findElement(By.xpath(xpath))
                    SearchType.ALL -> driver.findElements(By.xpath(xpath))
                }
            } catch (e: StaleElementReferenceException) {
                lastException = e
                this.waitFor()
            }
        } while (isNowBefore(end))
        throw lastException
    }

    fun execElementAction(
        xpath: String,
        driver: WebDriver,
        block: (element: WebElement) -> Any?
    ): Any? {
        val end = laterBy(TimeUnit.SECONDS.toMillis(timeWait))
        var lastException: StaleElementReferenceException
        do {
            try {
                return block(driver.findElement(By.xpath(xpath)))
            } catch (e: StaleElementReferenceException) {
                lastException = e
                this.waitFor()
            }
        } while (isNowBefore(end))
        throw lastException
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

        WaitElement(
            driver = driver,
            xpath = XpathBuilder.buildXpath(fullXpath),
            polling = pollingAnnotation,
            timeOutInSeconds = timeAnnotation
        ).waitCondition(conditionAnnotation)
    }
}

enum class SearchType {
    SINGLE,
    ALL
}
