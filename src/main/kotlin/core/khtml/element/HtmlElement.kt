package core.khtml.element

import core.khtml.ext.js
import core.khtml.utils.WebDriverUtils.execWebElementAction
import core.khtml.utils.WebDriverUtils.safeOperation
import core.khtml.waits.WaitCondition
import core.khtml.waits.WaitElement
import org.openqa.selenium.*
import org.openqa.selenium.interactions.Actions

class HtmlElement(val strXpath: String, private val driver: WebDriver) : BaseWebElement<HtmlElement>,
    BaseWaitElement<HtmlElement> {

    override val source: String
        get() = execWebElementAction(strXpath, driver) {
            it.getAttribute("innerHTML")
        } as String

    override val element: WebElement
        get() = safeOperation { driver.findElement(By.xpath(strXpath)) } as WebElement

    override val location: Point
        get() = execWebElementAction(strXpath, driver) {
            it.location
        } as Point

    override val text: String
        get() = execWebElementAction(strXpath, driver) {
            it.text
        } as String

    override val tagName: String
        get() = execWebElementAction(strXpath, driver) {
            it.tagName
        } as String

    override val isSelected: Boolean
        get() = execWebElementAction(strXpath, driver) {
            it.isSelected
        } as Boolean

    override val isEnabled: Boolean
        get() = execWebElementAction(strXpath, driver) {
            it.isEnabled
        } as Boolean

    override val size: Dimension
        get() = execWebElementAction(strXpath, driver) {
            it.size
        } as Dimension

    override val rect: Rectangle
        get() = execWebElementAction(strXpath, driver) {
            it.size
        } as Rectangle

    override val isDisplayed: Boolean
        get() = execWebElementAction(strXpath, driver) {
            it.isDisplayed
        } as Boolean

    override val exists: Boolean
        get() = try {
            execWebElementAction(strXpath, driver) {
                it.isDisplayed
            } as Boolean
        } catch (ignored: NoSuchElementException) {
            false
        }

    override val isDisplayedOnJs: Boolean
        get() {
            return driver.js("function isVisible(e) {\n" +
                    "    return !!( e.offsetWidth || e.offsetHeight || e.getClientRects().length );\n" +
                    "}; " +
                    "return isVisible(arguments[0])",element).toString().toBoolean()
        }

    override fun addClass(className: String): HtmlElement {
        driver.js("arguments[0].classList.add('$className');", element)
        return this
    }

    override fun removeClass(className: String): HtmlElement {
        driver.js("arguments[0].classList.remove('$className');", element)
        return this
    }

    override fun move(): HtmlElement {
        Actions(driver).moveToElement(element).perform()
        return this
    }

    override fun show(): HtmlElement {
        driver.js("arguments[0].style.display = 'block'", element)
        return this
    }

    override fun hide(): HtmlElement {
        driver.js("arguments[0].style.display = 'none'", element)
        return this
    }

    override fun wait(condition: WaitCondition, timeOut: Long): HtmlElement {
        WaitElement(
            timeOutInSeconds = timeOut,
            xpath = this.strXpath,
            driver = this.driver
        ).waitCondition(condition)
        return this
    }

    override fun waitCustomCondition(timeOut: Long, condition: (xpath: String) -> Boolean): HtmlElement {
        WaitElement(
            timeOutInSeconds = timeOut,
            xpath = this.strXpath,
            driver = this.driver
        ).waitCustomCondition { condition(this.strXpath) }
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



    override fun attr(name: String): String {
        return execWebElementAction(strXpath, driver) {
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
        return execWebElementAction(strXpath, driver) {
            it.getCssValue(name)
        } as String
    }

    override fun click(): HtmlElement {
        execWebElementAction(strXpath, driver) {
            it.click()
        }
        return this
    }

    override fun submit(): HtmlElement {
        execWebElementAction(strXpath, driver) {
            it.submit()
        }
        return this
    }

    override fun sendKeys(vararg keysToSend: CharSequence): HtmlElement {
        execWebElementAction(strXpath, driver) {
            it.sendKeys(*keysToSend)
        }
        return this
    }

    override fun setValue(value: String): HtmlElement {
        this.clear()
        this.sendKeys(value)
        return this
    }

    override fun clear(): HtmlElement {
        execWebElementAction(strXpath, driver) {
            it.clear()
        }
        return this
    }

    override fun toString(): String {
        return element.toString()
    }
}