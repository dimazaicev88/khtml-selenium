package core.khtml.element

import core.khtml.utils.WebDriverUtils.execWebElementAction
import org.openqa.selenium.WebDriver

open class TextInput(private val strXpath: String, driver: WebDriver) : CustomElement<TextInput>(strXpath, driver) {

    val inputValue: String
        get() = execWebElementAction(strXpath, driver) {
            it.getAttribute("value")
        } as String
}