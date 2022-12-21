package org.intsite.khtml.element

import org.openqa.selenium.WebDriver

class HtmlElement(xpath: String = " ", driver: WebDriver) : CustomElement<HtmlElement>(xpath, driver)