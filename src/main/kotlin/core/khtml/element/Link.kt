package core.khtml.element


import core.khtml.dump.DumpInfo
import core.khtml.utils.WebDriverUtils.execWebElementAction
import org.openqa.selenium.WebDriver

class Link(val strXpath: String, driver: WebDriver, private val dumpInfo: DumpInfo? = null) :
    CustomElement<Link>(strXpath, driver) {

    val reference: String
        get() = execWebElementAction(strXpath, driver, dumpInfo) {
            it.getAttribute("href")
        } as String
}
