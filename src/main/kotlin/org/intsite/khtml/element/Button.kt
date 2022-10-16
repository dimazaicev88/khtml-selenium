package org.intsite.khtml.element

import org.intsite.khtml.utils.WebDriverUtils
import org.openqa.selenium.WebDriver

open class Button(xpath: String, driver: WebDriver, testName: String? = null) : CustomElement<Button>(xpath, driver, testName) {

    @Suppress("UNCHECKED_CAST")
    fun submit(): Button {
        WebDriverUtils.execWebElementAction(xpath, driver) {
            it.submit()
        }
        return this
    }
}
