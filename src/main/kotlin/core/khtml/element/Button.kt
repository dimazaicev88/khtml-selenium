package core.khtml.element

import core.khtml.utils.WebDriverUtils.execWebElementAction
import org.openqa.selenium.WebDriver

open class Button(_xpath: String, driver: WebDriver) : CustomElement<Button>(_xpath, driver) {

    @Suppress("UNCHECKED_CAST")
    fun submit(): Button {
        execWebElementAction(xpath, driver) {
            it.submit()
        }
        return this
    }
}
