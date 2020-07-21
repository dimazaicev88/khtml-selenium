package org.intsite.khtml.element

import org.intsite.khtml.utils.WebDriverUtils.execWebElementAction
import org.openqa.selenium.WebDriver

class CheckBox(_xpath: String, driver: WebDriver, testName: String? = null) : CustomElement<CheckBox>(_xpath, driver, testName) {

    fun select(): CheckBox {
        if (!isSelected) {
            execWebElementAction(xpath, driver) {
                JsExecutor(xpath, driver, this).click()
            }
        }
        return this
    }

    fun deselect(): CheckBox {
        if (isSelected) {
            execWebElementAction(xpath, driver) {
                JsExecutor(xpath, driver, this).click()
            }
        }
        return this
    }

    fun set(value: Boolean): CheckBox {
        if (value) {
            select()
        } else {
            deselect()
        }
        return this
    }
}