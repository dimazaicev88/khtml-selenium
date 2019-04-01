package core.khtml.element

import core.khtml.ext.js
import core.khtml.utils.SearchType
import core.khtml.utils.WebDriverUtils.searchWebElement
import core.khtml.waits.WaitCondition
import core.khtml.waits.WaitElement
import org.openqa.selenium.*

open class CustomElement<T> constructor(private val xpath: String, val driver: WebDriver) :
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
        get() {
            return searchWebElement(driver, xpath, SearchType.SINGLE) as WebElement
        }

    override val exists: Boolean
        get() = try {
            element.isDisplayed
        } catch (ignored: NoSuchElementException) {
            false
        }

    override val source: String
        get() = element.getAttribute("innerHTML")

    override val location: Point
        get() = element.location

    override val text: String
        get() = element.text

    override val tagName: String
        get() = element.tagName

    override val isSelected: Boolean
        get() = element.isSelected

    override val isEnabled: Boolean
        get() = element.isEnabled

    override val size: Dimension
        get() = element.size

    override val rect: Rectangle
        get() = element.rect

    override val isDisplayed: Boolean
        get() = element.isDisplayed

    @Suppress("UNCHECKED_CAST")
    override fun setValue(value: String): T {
        element.clear()
        element.sendKeys(value)
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
        element.click()
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    override fun submit(): T {
        element.submit()
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    override fun sendKeys(vararg keysToSend: CharSequence): T {
        element.sendKeys(*keysToSend)
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    override fun clear(): T {
        element.clear()
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
        return element.getAttribute(name)
    }

    override fun findElements(xpath: String): List<WebElement> {
        return element.findElements(By.xpath(xpath))
    }

    override fun findElement(xpath: String): WebElement {
        return element.findElement(By.xpath(xpath))
    }

    override fun cssValue(name: String): String {
        return element.getCssValue(name)
    }
}