package org.intsite.khtml.element

import org.intsite.khtml.utils.WebDriverUtils
import org.openqa.selenium.WebDriver

open class Button(xpath: String, driver: WebDriver) : CustomElement<Button>(xpath, driver) {

    fun submit(): Button {
        WebDriverUtils.execWebElementAction(xpath, driver) {
            it.submit()
        }
        return this
    }
}
