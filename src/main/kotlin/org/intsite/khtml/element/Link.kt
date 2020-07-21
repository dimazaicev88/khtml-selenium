package org.intsite.khtml.element


import org.intsite.khtml.utils.WebDriverUtils.execWebElementAction
import org.openqa.selenium.WebDriver

class Link(_xpath: String, driver: WebDriver, testName: String? = null) : CustomElement<Link>(_xpath, driver, testName) {

    val reference: String
        get() = execWebElementAction(xpath, driver) {
            it.getAttribute("href")
        } as String
}
