package org.intsite.khtml.element

import org.openqa.selenium.WebDriver

class Text(xpath: String, driver: WebDriver, testName: String? = null) : CustomElement<Text>(xpath, driver, testName)