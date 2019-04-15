package core.khtml.element

import core.khtml.utils.WebDriverUtils.execWebElementAction
import org.openqa.selenium.WebDriver


class Image(private val strXpath: String, driver: WebDriver) : CustomElement<Image>(strXpath, driver) {

    val src: String
        get() = execWebElementAction(strXpath, driver) {
            it.getAttribute("src")
        } as String

    val alt: String
        get() = execWebElementAction(strXpath, driver) {
            it.getAttribute("alt")
        } as String
}