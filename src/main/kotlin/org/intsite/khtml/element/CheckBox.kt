package org.intsite.khtml.element

import org.intsite.khtml.utils.WebDriverUtils.execWebElementAction
import org.openqa.selenium.WebDriver

class CheckBox(xpath: String, driver: WebDriver) :
    CustomElement<CheckBox>(xpath, driver) {

    private val jsExecutor = JsExecutor(this.xpath, driver, this)

    fun select(): CheckBox {
        if (!isSelected) {
            execWebElementAction(xpath, driver) {
                jsExecutor.click()
            }
        }
        return this
    }

    fun deselect(): CheckBox {
        if (isSelected) {
            execWebElementAction(xpath, driver) {
                jsExecutor.click()
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