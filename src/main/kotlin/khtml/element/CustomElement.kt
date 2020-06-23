package khtml.element

import khtml.utils.WebDriverUtils
import khtml.utils.WebDriverUtils.execWebElementAction
import khtml.waits.WaitElement
import org.openqa.selenium.*
import org.openqa.selenium.interactions.Actions
import java.lang.Thread.sleep

abstract class CustomElement<T>(val xpath: String, val driver: WebDriver) {
    val wait = WaitElement(driver = driver, xpath = xpath)

    val jse: JsExecutor<T>
        get() = JsExecutor(xpath, driver, this as T)

    val element: WebElement
        get() = WebDriverUtils.safeOperation {
            driver.findElement(By.xpath(xpath))
        } as WebElement

    val exists: Boolean
        get() {
            val elements = WebDriverUtils.safeOperation {
                driver.findElements(By.xpath(xpath))
            } as List<WebElement>
            return elements.isNotEmpty()
        }

    val notExists: Boolean
        get() {
            val elements = WebDriverUtils.safeOperation {
                driver.findElements(By.xpath(xpath))
            } as List<WebElement>
            return elements.isEmpty()
        }

    @Suppress("CAST_NEVER_SUCCEEDS")
    val source: String
        get() = execWebElementAction(xpath, driver) {
            it.getAttribute("innerHTML")
        } as String

    val location: Point
        get() = execWebElementAction(xpath, driver) {
            it.location
        } as Point

    val text: String
        get() = execWebElementAction(xpath, driver) {
            it.text
        } as String

    val tagName: String
        get() = execWebElementAction(xpath, driver) {
            it.tagName
        } as String

    val isSelected: Boolean
        get() = execWebElementAction(xpath, driver) {
            it.isSelected
        } as Boolean

    val isEnabled: Boolean
        get() = execWebElementAction(xpath, driver) {
            it.isEnabled
        } as Boolean

    val size: Dimension
        get() = execWebElementAction(xpath, driver) {
            it.size
        } as Dimension

    val rect: Rectangle
        get() = execWebElementAction(xpath, driver) {
            it.size
        } as Rectangle

    val isDisplayed: Boolean
        get() {
            return try {
                execWebElementAction(xpath, driver) {
                    it.isDisplayed
                } as Boolean
            } catch (e: NoSuchElementException) {
                false
            }
        }

    @Suppress("UNCHECKED_CAST")
    fun move(): T {
        Actions(driver).moveToElement(element).perform()
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    fun click(): T {
        execWebElementAction(xpath, driver) {
            it.click()
        }
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    fun clickIfExists(): T {
        if (jse.exists) {
            this.click()
        }
        return this as T
    }

    fun attr(name: String): String {
        return execWebElementAction(xpath, driver) {
            it.getAttribute(name)
        } as String? ?: ""
    }

    fun findElements(xpath: String): List<WebElement> {
        return element.findElements(By.xpath(xpath))
    }

    fun findElement(xpath: String): WebElement {
        return element.findElement(By.xpath(xpath))
    }

    fun cssValue(name: String): String {
        return execWebElementAction(xpath, driver) {
            it.getCssValue(name)
        } as String? ?: ""
    }

    fun clickIfExists(pause: Long = 0) {
        if (jse.exists) {
            sleep(pause)
            this.click()
        }
    }

    fun repeatClick(repeat: Int, timeOut: Long, refresh: Boolean, condition: () -> Boolean): T {
        for (i in (0..repeat)) {
            try {
                execWebElementAction(xpath, driver) {
                    it.click()
                }
                wait.waitCustomCondition(timeOut, 500, condition = condition)
                break
            } catch (ignore: Exception) {
                if (refresh) driver.navigate().refresh()
            }
        }
        return this as T
    }
}