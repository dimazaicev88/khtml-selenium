package core.khtml.element

import core.khtml.ext.js
import core.khtml.utils.SearchType
import core.khtml.utils.WebDriverUtils.searchWebElement
import core.khtml.waits.WaitCondition
import core.khtml.waits.WaitElement
import org.openqa.selenium.*

class HtmlElement(private var xpath: String, private val driver: WebDriver) : BaseWebElement<HtmlElement>,
    BaseWaitElement<HtmlElement> {

    override fun wait(condition: WaitCondition, timeOut: Long): HtmlElement {
        WaitElement(
            timeOutInSeconds = timeOut,
            xpath = this.xpath,
            driver = this.driver
        ).waitCondition(condition)
        return this
    }

    override fun waitCustomCondition(timeOut: Long, condition: (xpath: String) -> Boolean): HtmlElement {
        WaitElement(
            timeOutInSeconds = timeOut,
            xpath = this.xpath,
            driver = this.driver
        ).waitCustomCondition(condition(this.xpath))
        return this
    }

    override fun jsChange(): HtmlElement {
        driver.js("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", element)
        return this
    }

    override fun jsBlur(): HtmlElement {
        driver.js("arguments[0].dispatchEvent(new Event('blur', { bubbles: true }));", element)
        return this
    }

    override fun jsClick(): HtmlElement {
        driver.js("arguments[0].dispatchEvent(new Event('click', { bubbles: true }));", element)
        return this
    }

    override fun jsFocus(): HtmlElement {
        driver.js("arguments[0].dispatchEvent(new Event('focus', { bubbles: true }));", element)
        return this
    }

    override val source: String
        get() = element.getAttribute("innerHTML")

    override val element: WebElement
        get() = searchWebElement(driver, xpath, SearchType.SINGLE) as WebElement

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

    override val exists: Boolean
        get() = try {
            element.isDisplayed
        } catch (ignored: NoSuchElementException) {
            false
        }

    override fun attr(name: String): String {
        return element.getAttribute("name")
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

    override fun click(): HtmlElement {
        element.click()
        return this
    }

    override fun submit(): HtmlElement {
        element.submit()
        return this
    }

    override fun sendKeys(vararg charSequences: CharSequence): HtmlElement {
        element.sendKeys(*charSequences)
        return this
    }

    override fun setValue(value: String): HtmlElement {
        this.clear()
        this.sendKeys(value)
        return this
    }

    override fun clear(): HtmlElement {
        element.clear()
        return this
    }

    override fun toString(): String {
        return element.toString()
    }
}