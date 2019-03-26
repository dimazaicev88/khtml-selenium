package core.khtml.element


import org.openqa.selenium.WebDriver

class Link(private val strXpath: String, driver: WebDriver) : CustomElement<Link>(strXpath, driver) {

    val reference: String
        get() = element.getAttribute("href")
}
