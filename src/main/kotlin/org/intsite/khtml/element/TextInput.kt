package org.intsite.khtml.element

import org.intsite.khtml.throttle.Throttle
import org.intsite.khtml.utils.WebDriverUtils.execWebElementAction
import org.openqa.selenium.WebDriver

open class TextInput(xpath: String, driver: WebDriver) :
    CustomElement<TextInput>(xpath, driver) {

    val inputValue: String
        get() {
            Thread.sleep(Throttle.timeBeforeInputValue(driver))
            val result = execWebElementAction(xpath, driver) {
                it.getAttribute("value")
            } as String
            Thread.sleep(Throttle.timeAfterInputValue(driver))
            return result
        }

    fun setValue(value: CharSequence): TextInput {
        this.clear()
        this.sendKeys(value)
        return this
    }

    fun sendKeys(vararg keysToSend: CharSequence): TextInput {
        Thread.sleep(Throttle.timeBeforeSetValue(driver))
        execWebElementAction(xpath, driver) {
            it.sendKeys(*keysToSend)
        }
        Thread.sleep(Throttle.timeAfterSetValue(driver))
        return this
    }

    fun clear(): TextInput {
        Thread.sleep(Throttle.timeBeforeClear(driver))
        execWebElementAction(xpath, driver) {
            it.clear()
        }
        Thread.sleep(Throttle.timeAfterClear(driver))
        return this
    }
}