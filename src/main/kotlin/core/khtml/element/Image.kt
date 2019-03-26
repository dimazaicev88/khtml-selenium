package core.khtml.element

import org.openqa.selenium.WebDriver


class Image(private val strXpath: String, driver: WebDriver) : CustomElement<Image>(strXpath, driver) {

    val src: String
        get() = element.getAttribute("src")

    val alt: String
        get() = element.getAttribute("alt")
}