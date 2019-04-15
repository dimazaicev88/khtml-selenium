package core.khtml.utils

import core.khtml.annotations.Wait
import core.khtml.build.XpathBuilder
import core.khtml.conf.FullXpath
import core.khtml.dump.DumpCondition
import core.khtml.dump.DumpInfo
import core.khtml.ext.saveScreenshot
import core.khtml.waits.WaitElement
import org.openqa.selenium.*
import java.io.File
import java.lang.reflect.Method
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.TimeUnit

object WebDriverUtils {
    private const val timeWait: Long = 30
    private const val sleepFor = 500L

    fun safeOperation(block: () -> Any): Any {
        val end = laterBy(TimeUnit.SECONDS.toMillis(timeWait))
        var lastException: StaleElementReferenceException
        do {
            try {
                return block()
            } catch (e: StaleElementReferenceException) {
                lastException = e
                this.waitFor()
            }
        } while (isNowBefore(end))
        throw lastException

    }

    fun execWebElementAction(
        xpath: String,
        driver: WebDriver,
        dumpInfo: DumpInfo? = null,
        block: (element: WebElement) -> Any?
    ): Any? {
        return if (dumpInfo != null) {
            dump(xpath, driver, dumpInfo) {
                safeOperation {
                    block(driver.findElement(By.xpath(xpath)))!!
                }
            }
        } else {
            safeOperation {
                block(driver.findElement(By.xpath(xpath)))!!
            }
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

        WaitElement(
            driver = driver,
            xpath = XpathBuilder.buildXpath(fullXpath),
            polling = pollingAnnotation,
            timeOutInSeconds = timeAnnotation
        ).waitCondition(conditionAnnotation)
    }

    fun dump(xpath: String, driver: WebDriver, dumpInfo: DumpInfo, block: () -> Any?): Any? {
        val fileName =
            "${dumpInfo.method.declaringClass.canonicalName.replace(
                ".",
                "_"
            )}_${dumpInfo.method.name}_${UUID.randomUUID().toString().substring(0, 5)}"

        fun createDump() {
            driver.saveScreenshot(dumpInfo.dir, fileName)
            val path = Paths.get(dumpInfo.dir, "$fileName.txt")
            File(path.toString()).createNewFile()
            File(path.toString()).writeText("xpath:$xpath\nsource:  ${driver.pageSource}")
        }

        if (dumpInfo.dir.isEmpty()) {
            throw RuntimeException("Directory for save dump not set")
        }
        if (dumpInfo.condition == DumpCondition.FAIL) {
            try {
                return block()
            } catch (e: ElementNotVisibleException) {
                createDump()
                throw e
            } catch (e: ElementNotInteractableException) {
                createDump()
                throw e
            } catch (e: WebDriverException) {
                createDump()
                throw e
            }
        } else {
            createDump()
            return block()
        }
    }
}

