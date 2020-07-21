package org.intsite.khtml.element

import org.intsite.khtml.utils.WebDriverUtils
import org.openqa.selenium.WebDriver

open class Button(_xpath: String, driver: WebDriver, testName: String? = null) : CustomElement<Button>(_xpath, driver, testName) {

    @Suppress("UNCHECKED_CAST")
    fun submit(): Button {
        WebDriverUtils.execWebElementAction(xpath, driver) {
            it.submit()
        }
        return this
    }
}
