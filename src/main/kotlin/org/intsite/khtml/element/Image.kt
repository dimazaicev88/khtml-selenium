package org.intsite.khtml.element

import org.intsite.khtml.utils.WebDriverUtils.execWebElementAction
import org.openqa.selenium.WebDriver


class Image(xpath: String, driver: WebDriver) : CustomElement<Image>(xpath, driver) {

    val src: String
        get() = execWebElementAction(xpath, driver) {
            it.getAttribute("src")
        } as String

    val alt: String
        get() = execWebElementAction(xpath, driver) {
            it.getAttribute("alt")
        } as String
}