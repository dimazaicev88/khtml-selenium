package core.khtml.element

import core.khtml.dump.DumpInfo
import core.khtml.utils.WebDriverUtils.execWebElementAction
import org.openqa.selenium.WebDriver

open class TextInput(private val strXpath: String, driver: WebDriver, private val dumpInfo: DumpInfo? = null) :
    CustomElement<TextInput>(strXpath, driver) {

    val inputValue: String
        get() = execWebElementAction(strXpath, driver, dumpInfo) {
            it.getAttribute("value")
        } as String
}