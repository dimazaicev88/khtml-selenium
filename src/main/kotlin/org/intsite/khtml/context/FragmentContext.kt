package org.intsite.khtml.context

import org.intsite.khtml.annotations.InjectContext
import org.intsite.khtml.annotations.InjectWebDriver
import org.intsite.khtml.element.HtmlElement
import org.openqa.selenium.WebDriver

interface FragmentContext {

    @InjectWebDriver
    fun driver(): WebDriver

    @InjectContext
    fun context(): HtmlElement
}
