package org.intsite.khtml.element

import org.intsite.khtml.utils.WebDriverUtils.execWebElementAction
import org.openqa.selenium.WebDriver

open class TextInput(_xpath: String, driver: WebDriver, testName: String? = null) : CustomElement<TextInput>(_xpath, driver, testName) {

    val inputValue: String
        get() = execWebElementAction(xpath, driver) {
            it.getAttribute("value")
        } as String

    @Suppress("UNCHECKED_CAST")
    fun setValue(value: CharSequence): TextInput {
        this.clear()
        this.sendKeys(value)
        return this
    }

    @Suppress("UNCHECKED_CAST")
    fun sendKeys(vararg keysToSend: CharSequence): TextInput {
        execWebElementAction(xpath, driver) {
            it.sendKeys(*keysToSend)
        }
        return this
    }

    @Suppress("UNCHECKED_CAST")
    fun clear(): TextInput {
        execWebElementAction(xpath, driver) {
            it.clear()
        }
        return this
    }
}