package core.khtml.element

import org.openqa.selenium.WebDriver

open class TextInput(private val strXpath: String, driver: WebDriver) : CustomElement<TextInput>(strXpath, driver) {

    val inputValue: String
        get() {
            return this.element.getAttribute("value")
        }
}