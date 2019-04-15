package core.khtml.element


import core.khtml.utils.WebDriverUtils
import core.khtml.utils.WebDriverUtils.execWebElementAction
import org.openqa.selenium.WebDriver

class Link(val strXpath: String, driver: WebDriver) : CustomElement<Link>(strXpath, driver) {

    val reference: String
        get() = execWebElementAction(strXpath, driver) {
            it.getAttribute("href")
        } as String
}
