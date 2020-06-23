package khtml.element

import khtml.utils.WebDriverUtils.execWebElementAction
import org.openqa.selenium.WebDriver


class Image(_xpath: String, driver: WebDriver, testName: String? = null) : CustomElement<Image>(_xpath, driver, testName) {

    val src: String
        get() = execWebElementAction(xpath, driver) {
            it.getAttribute("src")
        } as String

    val alt: String
        get() = execWebElementAction(xpath, driver) {
            it.getAttribute("alt")
        } as String
}