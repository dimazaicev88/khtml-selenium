package khtml.element


import khtml.utils.WebDriverUtils.execWebElementAction
import org.openqa.selenium.WebDriver

class Link(_xpath: String, driver: WebDriver) : CustomElement<Link>(_xpath, driver) {

    val reference: String
        get() = execWebElementAction(xpath, driver) {
            it.getAttribute("href")
        } as String
}
