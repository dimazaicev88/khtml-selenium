package org.intsite.khtml.element

import org.intsite.khtml.ext.js
import org.intsite.khtml.ext.jsFindElement
import org.intsite.khtml.throttle.Throttle
import org.intsite.khtml.utils.WebDriverUtils.safeOperation
import org.intsite.khtml.waits.WaitElement
import org.openqa.selenium.WebDriver
import java.time.Duration

class JsExecutor<T>(
    private val xpath: String,
    private val driver: WebDriver,
    private val element: T
) {

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
            return driver.js(
                """function isVisible(e) {
                if(e==null) return false;            
                    return !!( e.offsetWidth || e.offsetHeight || e.getClientRects().length );
                }; return isVisible(${driver.jsFindElement(xpath)}.singleNodeValue)"""
            ).toString().toBoolean()
        }

    fun addClass(className: String): T {
        WaitElement(driver, xpath).waitCustomCondition(polling = Duration.ofMillis(500), timeOut = Duration.ofSeconds(15)) {
            this.exists && this.isDisplayed
        }
        safeOperation {
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.classList.add('$className');")
        }
        return element
    }

    fun removeClass(className: String): T {
        WaitElement(driver, xpath).waitCustomCondition(polling = Duration.ofMillis(500), timeOut = Duration.ofSeconds(15)) {
            this.exists && this.isDisplayed
        }
        safeOperation {
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.classList.remove('$className');")
        }
        return element
    }

    val text: String
        get() {
            Thread.sleep(Throttle.timeBeforeGetText(driver))
            WaitElement(driver, xpath).waitCustomCondition(polling = Duration.ofMillis(500), timeOut = Duration.ofSeconds(15)) {
                this.exists && this.isDisplayed
            }
            val text = safeOperation {
                driver.js("return ${driver.jsFindElement(xpath)}.singleNodeValue.textContent")
            } as String?
            Thread.sleep(Throttle.timeAfterGetText(driver))
            return if (text.isNullOrEmpty()) "" else text
        }

    fun show(): T {
        Thread.sleep(Throttle.timeBeforeShowElement(driver))
        WaitElement(driver, xpath).waitCustomCondition(polling = Duration.ofMillis(500), timeOut = Duration.ofSeconds(15)) {
            this.exists
        }
        safeOperation {
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.style.display = 'block'")
        }
        Thread.sleep(Throttle.timeAfterShowElement(driver))
        return element
    }

    fun setValue(value: String): T {
        Thread.sleep(Throttle.timeBeforeSetValue(driver))
        WaitElement(driver, xpath).waitCustomCondition(
            polling = Duration.ofMillis(500),
            timeOut = Duration.ofSeconds(15)
        ) {
            this.exists
        }
        safeOperation {
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.value = '$value'")
        }
        Thread.sleep(Throttle.timeAfterSetValue(driver))
        return element
    }

    fun hide(): T {
        Thread.sleep(Throttle.timeBeforeHideElement(driver))
        WaitElement(driver, xpath).waitCustomCondition(polling = Duration.ofMillis(500), timeOut = Duration.ofSeconds(15)) {
            this.exists
        }
        safeOperation {
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.style.display = 'none'")
        }
        Thread.sleep(Throttle.timeAfterHideElement(driver))
        return element
    }

    fun change(): T {
        Thread.sleep(Throttle.timeBeforeChange(driver))
        WaitElement(driver, xpath).waitCustomCondition(polling = Duration.ofMillis(500), timeOut = Duration.ofSeconds(15)) {
            this.exists && this.isDisplayed
        }
        safeOperation {
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.dispatchEvent(new Event('change', { bubbles: true }))")
        }
        Thread.sleep(Throttle.timeAfterChange(driver))
        return element
    }

    fun blur(): T {
        Thread.sleep(Throttle.timeBeforeBlur(driver))
        WaitElement(driver, xpath).waitCustomCondition(polling = Duration.ofMillis(500), timeOut = Duration.ofSeconds(15)) {
            this.exists && this.isDisplayed
        }
        safeOperation {
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.dispatchEvent(new Event('blur', { bubbles: true }));")
        }
        Thread.sleep(Throttle.timeAfterBlur(driver))
        return element
    }

    fun click(): T {
        Thread.sleep(Throttle.timeBeforeClick(driver))
        WaitElement(driver, xpath).waitCustomCondition(polling = Duration.ofMillis(500), timeOut = Duration.ofSeconds(15)) {
            this.exists && this.isDisplayed
        }
        safeOperation {
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.style.background='lightblue';")
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.click();")
        }
        Thread.sleep(Throttle.timeAfterClick(driver))
        return element
    }

    fun click(checkDisplay: Boolean = true): T {
        Thread.sleep(Throttle.timeBeforeClick(driver))
        if (checkDisplay)
            WaitElement(driver, xpath).waitCustomCondition(polling = Duration.ofMillis(500), timeOut = Duration.ofSeconds(15)) {
                this.exists && this.isDisplayed
            }
        else
            WaitElement(driver, xpath).waitCustomCondition(polling = Duration.ofMillis(500), timeOut = Duration.ofSeconds(15)) {
                this.exists
            }
        safeOperation {
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.style.background='lightblue';")
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.click();")
        }
        Thread.sleep(Throttle.timeAfterClick(driver))
        return element
    }

    fun focus(): T {
        Thread.sleep(Throttle.timeBeforeFocus(driver))
        WaitElement(driver, xpath).waitCustomCondition(polling = Duration.ofMillis(500), timeOut = Duration.ofSeconds(15)) {
            this.exists && this.isDisplayed
        }
        safeOperation {
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.dispatchEvent(new Event('focus', { bubbles: true }));")
        }
        Thread.sleep(Throttle.timeAfterFocus(driver))
        return element
    }

    fun scrollTop(): T {
        safeOperation {
            driver.js("window.scrollTo(0,0)")
        }
        return element
    }

    fun scroll(): T {
        safeOperation {
            driver.js("arguments[0].scrollIntoView(true);")
        }
        return element
    }

    @Suppress("UNCHECKED_CAST")
    fun scrollBottom(): T {
        safeOperation {
            driver.js("arguments[0].scrollIntoView(false);", (element as CustomElement<HtmlElement>).element)
        }
        return element
    }

    fun mouseOver(): T {
        Thread.sleep(Throttle.timeBeforeMouseOver(driver))
        WaitElement(driver, xpath).waitCustomCondition(polling = Duration.ofMillis(500), timeOut = Duration.ofSeconds(15)) {
            this.exists && this.isDisplayed
        }
        safeOperation {
            driver.js("${driver.jsFindElement(xpath)}.singleNodeValue.dispatchEvent(new Event('mouseover', { bubbles: true }));")
        }
        Thread.sleep(Throttle.timeAfterMouseOver(driver))
        return element
    }
}