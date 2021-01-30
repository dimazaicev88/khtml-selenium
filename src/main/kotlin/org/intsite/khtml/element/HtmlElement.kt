package org.intsite.khtml.element

import org.openqa.selenium.WebDriver

class HtmlElement(xpath: String = " ", driver: WebDriver, testName: String? = null) : CustomElement<HtmlElement>(xpath, driver, testName)