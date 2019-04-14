package core.khtml.element

import core.khtml.dump.DumpInfo
import core.khtml.ext.js
import core.khtml.utils.WebDriverUtils.dump
import core.khtml.utils.WebDriverUtils.execWebElementAction
import core.khtml.utils.WebDriverUtils.safeOperation
import core.khtml.waits.WaitCondition
import core.khtml.waits.WaitElement
import org.openqa.selenium.*

open class CustomElement<T> constructor(
    private val xpath: String,
    val driver: WebDriver
) :
    BaseWebElement<T>,
    BaseWaitElement<T> {

    @Suppress("UNCHECKED_CAST")
    override fun wait(condition: WaitCondition, timeOut: Long): T {
        WaitElement(
            timeOutInSeconds = timeOut,
            xpath = this.xpath,
            driver = this.driver
        ).waitCondition(condition)
        return this as T
    }

    override val element: WebElement
        get() = safeOperation {
            driver.findElement(By.xpath(xpath))
        } as WebElement

    override val exists: Boolean
        get() = try {
            execWebElementAction(xpath, driver) {
                it.isDisplayed
            } as Boolean
        } catch (ignored: NoSuchElementException) {
            false
        }

    @Suppress("CAST_NEVER_SUCCEEDS")
    override val source: String
        get() = execWebElementAction(xpath, driver) {
            it.getAttribute("innerHTML")
        } as String

    override val location: Point
        get() = execWebElementAction(xpath, driver) {
            it.location
        } as Point

    override val text: String
        get() = execWebElementAction(xpath, driver) {
            it.text
        } as String

    override val tagName: String
        get() = execWebElementAction(xpath, driver) {
            it.tagName
        } as String

    override val isSelected: Boolean
        get() = execWebElementAction(xpath, driver) {
            it.isSelected
        } as Boolean

    override val isEnabled: Boolean
        get() = execWebElementAction(xpath, driver) {
            it.isEnabled
        } as Boolean

    override val size: Dimension
        get() = execWebElementAction(xpath, driver) {
            it.size
        } as Dimension

    override val rect: Rectangle
        get() = execWebElementAction(xpath, driver) {
            it.size
        } as Rectangle

    override val isDisplayed: Boolean
        get() = execWebElementAction(xpath, driver) {
            it.isDisplayed
        } as Boolean

    @Suppress("UNCHECKED_CAST")
    override fun setValue(value: String): T {
        this.clear()
        this.sendKeys(value)
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    override fun jsChange(): T {
        driver.js(" var element=arguments[0]; element.dispatchEvent(new Event('change', { bubbles: true }));")
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    override fun jsBlur(): T {
        driver.js(" var element=arguments[0]; element.dispatchEvent(new Event('blur', { bubbles: true }));")
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    override fun jsClick(): T {
        driver.js(" var element=arguments[0]; element.dispatchEvent(new Event('click', { bubbles: true }));")
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    override fun jsFocus(): T {
        driver.js(" var element=arguments[0]; element.dispatchEvent(new Event('focus', { bubbles: true }));")
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    override fun click(): T {
        execWebElementAction(xpath, driver) {
            it.click()
        }
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    override fun submit(): T {
        execWebElementAction(xpath, driver) {
            it.submit()
        }
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    override fun sendKeys(vararg keysToSend: CharSequence): T {
        execWebElementAction(xpath, driver) {
            it.sendKeys(*keysToSend)
        }
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    override fun clear(): T {
        execWebElementAction(xpath, driver) {
            it.clear()
        }
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    override fun waitCustomCondition(timeOut: Long, condition: (xpath: String) -> Boolean): T {
        WaitElement(
            timeOutInSeconds = timeOut,
            xpath = this.xpath,
            driver = this.driver
        ).waitCustomCondition(condition(this.xpath))
        return this as T
    }

    override fun attr(name: String): String {
        return execWebElementAction(xpath, driver) {
            it.getAttribute(name)
        } as String
    }

    override fun findElements(xpath: String): List<WebElement> {
        return element.findElements(By.xpath(xpath))
    }

    override fun findElement(xpath: String): WebElement {
        return element.findElement(By.xpath(xpath))
    }

    override fun cssValue(name: String): String {
        return execWebElementAction(xpath, driver) {
            it.getCssValue(name)
        } as String
    }
}