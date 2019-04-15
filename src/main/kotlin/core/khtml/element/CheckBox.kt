package core.khtml.element

import core.khtml.utils.WebDriverUtils.execWebElementAction
import org.openqa.selenium.WebDriver

class CheckBox(private val strXpath: String, driver: WebDriver) : CustomElement<CheckBox>(strXpath, driver) {

    fun select() {
        if (!isSelected) {
            execWebElementAction(strXpath, driver) {
                it.click()
            }
        }
    }

    fun deselect() {
        if (isSelected) {
            execWebElementAction(strXpath, driver) {
                it.click()
            }
        }
    }

    fun set(value: Boolean) {
        if (value) {
            select()
        } else {
            deselect()
        }
    }
}