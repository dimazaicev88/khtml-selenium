package khtml.context

import khtml.annotations.InjectContext
import khtml.annotations.InjectWebDriver
import khtml.element.HtmlElement
import org.openqa.selenium.WebDriver

interface FragmentContext {

    @InjectWebDriver
    fun driver(): WebDriver

    @InjectContext
    fun context(): HtmlElement
}
