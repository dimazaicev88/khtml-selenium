package khtml.element

import khtml.utils.WebDriverUtils.execWebElementAction
import org.openqa.selenium.WebDriver

class CheckBox(_xpath: String, driver: WebDriver) : CustomElement<CheckBox>(_xpath, driver) {

    fun select(): CheckBox {
        if (!isSelected) {
            execWebElementAction(xpath, driver) {
                it.click()
            }
        }
        return this
    }

    fun deselect(): CheckBox {
        if (isSelected) {
            execWebElementAction(xpath, driver) {
                it.click()
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