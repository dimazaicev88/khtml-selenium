package org.intsite.khtml.element

import org.openqa.selenium.WebDriver

class HtmlElement(_xpath: String = " ", driver: WebDriver, testName: String? = null) : CustomElement<HtmlElement>(_xpath, driver, testName)