package core.khtml.element

import core.khtml.ext.js
import core.khtml.ext.jsFindElement
import core.khtml.utils.WebDriverUtils.safeOperation
import core.khtml.waits.WaitElement
import org.openqa.selenium.WebDriver

class JsExecutor<T>(private val xpath: String, private val driver: WebDriver, private val element: T) {

    val notExists: Boolean
        get() = safeOperation {
            driver.js("return ${driver.jsFindElement(xpath)}.singleNodeValue==null")
        } as Boolean

    val exists: Boolean
        get() = safeOperation {
            driver.js("return ${driver.jsFindElement(xpath)}.singleNodeValue!=null")
        } as Boolean

    val isDisplayed: Boolean
        get() {
            return driver.js("function isVisible(e) {\n" +
                    "    return !!( e.offsetWidth || e.offsetHeight || e.getClientRects().length );\n" +
                    "}; " +
                    "return isVisible(${driver.jsFindElement(xpath)}.singleNodeValue)").toString().toBoolean()
        }

    @Suppress("UNCHECKED_CAST")
    fun addClass(className: String): T {
        WaitElement(driver, xpath).waitCustomCondition(polling = 50, timeOut = 5) {
            this.exists && this.isDisplayed
        }
        safeOperation {
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.classList.add('$className');")
        }
        return element
    }

    @Suppress("UNCHECKED_CAST")
    fun removeClass(className: String): T {
        WaitElement(driver, xpath).waitCustomCondition(polling = 50, timeOut = 5) {
            this.exists && this.isDisplayed
        }
        safeOperation {
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.classList.remove('$className');")
        }
        return element
    }

    val text: String
        get() {
            WaitElement(driver, xpath).waitCustomCondition(polling = 50, timeOut = 5) {
                this.exists && this.isDisplayed
            }
            val text = safeOperation {
                driver.js("return ${driver.jsFindElement(xpath)}.singleNodeValue.textContent")
            } as String?
            return if (text.isNullOrEmpty()) "" else text
        }

    @Suppress("UNCHECKED_CAST")
    fun show(): T {
        WaitElement(driver, xpath).waitCustomCondition(polling = 50, timeOut = 5) {
            this.exists
        }
        safeOperation {
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.style.display = 'block'")
        }
        return element
    }

    @Suppress("UNCHECKED_CAST")
    fun setValue(value: String): T {
        WaitElement(driver, xpath).waitCustomCondition(polling = 50, timeOut = 5) {
            this.exists
        }
        safeOperation {
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.value = '$value'")
        }
        return element
    }

    @Suppress("UNCHECKED_CAST")
    fun hide(): T {
        WaitElement(driver, xpath).waitCustomCondition(polling = 50, timeOut = 5) {
            this.exists
        }
        safeOperation {
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.style.display = 'none'")
        }
        return element
    }

    @Suppress("UNCHECKED_CAST")
    fun change(): T {
        WaitElement(driver, xpath).waitCustomCondition(polling = 50, timeOut = 5) {
            this.exists && this.isDisplayed
        }
        safeOperation {
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.dispatchEvent(new Event('change', { bubbles: true }))")
        }
        return element
    }

    @Suppress("UNCHECKED_CAST")
    fun blur(): T {
        WaitElement(driver, xpath).waitCustomCondition(polling = 50, timeOut = 5) {
            this.exists && this.isDisplayed
        }
        safeOperation {
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.dispatchEvent(new Event('blur', { bubbles: true }));")
        }
        return element
    }

    @Suppress("UNCHECKED_CAST")
    fun click(): T {
        WaitElement(driver, xpath).waitCustomCondition(polling = 50, timeOut = 5) {
            this.exists && this.isDisplayed
        }
        safeOperation {
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.style.background='lightblue';")
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.click();")
        }
        return element
    }

    @Suppress("UNCHECKED_CAST")
    fun focus(): T {
        WaitElement(driver, xpath).waitCustomCondition(polling = 50, timeOut = 5) {
            this.exists && this.isDisplayed
        }
        safeOperation {
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.dispatchEvent(new Event('focus', { bubbles: true }));")
        }
        return element
    }

    @Suppress("UNCHECKED_CAST")
    fun scrollTop(): T {
        driver.js("window.scrollTo(0,0)")
        return element
    }

    @Suppress("UNCHECKED_CAST")
    fun scroll(): T {
        safeOperation {
            driver.js("arguments[0].scrollIntoView(true);")
        }
        return element
    }
}