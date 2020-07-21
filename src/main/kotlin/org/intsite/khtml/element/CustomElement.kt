package org.intsite.khtml.element

import org.intsite.khtml.ext.js
import org.intsite.khtml.ext.localStorage
import org.intsite.khtml.ext.refresh
import org.intsite.khtml.utils.MapTests
import org.intsite.khtml.utils.WebDriverUtils.execWebElementAction
import org.intsite.khtml.utils.WebDriverUtils.safeOperation
import org.intsite.khtml.waits.WaitElement
import org.openqa.selenium.*
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.FluentWait
import java.lang.Thread.sleep
import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger

abstract class CustomElement<T>(val xpath: String, val driver: WebDriver, val testName: String? = null) {
    private val mapTests = MapTests()
    val wait = WaitElement(driver = driver, xpath = xpath)
    val actionId: AtomicInteger = AtomicInteger(0)
    private val _object = Any()

    val jse: JsExecutor<T>
        get() = JsExecutor(xpath, driver, this as T, testName)

    val element: WebElement
        get() = safeOperation {
            driver.findElement(By.xpath(xpath))
        } as WebElement

    val exists: Boolean
        get() {
            val elements = safeOperation {
                driver.findElements(By.xpath(xpath))
            } as List<WebElement>
            return elements.isNotEmpty()
        }

    val notExists: Boolean
        get() {
            val elements = safeOperation {
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
            mapTests.add(xpath, testName, driver)
            it.text
        } as String

    val tagName: String
        get() = execWebElementAction(xpath, driver) {
            it.tagName
        } as String

    val isSelected: Boolean
        get() = execWebElementAction(xpath, driver) {
            mapTests.add(xpath, testName, driver)
            it.isSelected
        } as Boolean

    val isEnabled: Boolean
        get() = execWebElementAction(xpath, driver) {
            mapTests.add(xpath, testName, driver)
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
        val action = Actions(driver)
        safeOperation {
            mapTests.add(xpath, testName, driver)
            action.moveToElement(element).perform()
        }
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    fun click(): T {
        execWebElementAction(xpath, driver) {
            mapTests.add(xpath, testName, driver)
            it.click()
        }
        return this as T
    }

    fun removeItemInLocalStorage(key: String) {
        synchronized(_object) {
            driver.localStorage.removeItem(key)
        }
    }

    fun updateLocalStorage(key: String, value: String) {
        synchronized(_object) {
            driver.js("""localStorage.setItem('$key', "$value");""")
        }
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
            mapTests.add(xpath, testName, driver)
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
            mapTests.add(xpath, testName, driver)
            it.getCssValue(name)
        } as String? ?: ""
    }

    fun clickIfExists(pause: Long = 0) {
        if (jse.exists) {
            sleep(pause)
            this.click()
        }
    }

    fun repeatClick(repeat: Int = 15, timeOut: Long = 2, refresh: Boolean = true, polling: Long = 1000, condition: () -> Boolean): T {
        val wait = FluentWait(driver)
                .withTimeout(Duration.ofSeconds(timeOut))
                .pollingEvery(Duration.ofMillis(polling))
                .ignoreAll(
                        listOf(
                                org.openqa.selenium.NoSuchElementException::class.java,
                                StaleElementReferenceException::class.java
                        )
                )
        for (i in (0..repeat)) {
            try {
                execWebElementAction(xpath, driver) {
                    it.click()
                }
                wait.until { condition() }
                return this as T
            } catch (ignore: Exception) {
                if (refresh) driver.refresh()
            }
        }
        return this as T
    }
}