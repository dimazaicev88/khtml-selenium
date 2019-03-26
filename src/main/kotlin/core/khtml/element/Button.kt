package core.khtml.element

import org.openqa.selenium.WebDriver

open class Button(private val strXpath: String, driver: WebDriver) : CustomElement<Button>(strXpath, driver)
