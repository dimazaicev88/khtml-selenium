package example

import core.khtml.annotations.Element
import core.khtml.annotations.Fragment
import core.khtml.context.FragmentContext
import core.khtml.element.Image
import org.openqa.selenium.WebElement

@Fragment(".//header[contains(@class,'masthead')]")
interface Header : FragmentContext {

    @Element(".//img[contains(@class,'img-fluid')2]")
    fun logoImg(): Image

    fun defaultMethod(): WebElement {
        return context().element
    }

    fun defaultMethod(string: String) {
        println(string)
    }
}
