package core.khtml.context

import core.khtml.annotations.InjectContext
import core.khtml.annotations.InjectWebDriver
import core.khtml.element.HtmlElement
import org.openqa.selenium.WebDriver

interface FragmentContext {

    @InjectWebDriver
    fun driver(): WebDriver

    @InjectContext
    fun context(): HtmlElement
}
