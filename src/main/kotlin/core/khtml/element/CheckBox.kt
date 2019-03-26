package core.khtml.element

import org.openqa.selenium.WebDriver

class CheckBox(private val strXpath: String, driver: WebDriver) : CustomElement<CheckBox>(strXpath, driver) {

    fun select() {
        if (!isSelected) {
            element.click()
        }
    }

    fun deselect() {
        if (isSelected) {
            element.click()
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