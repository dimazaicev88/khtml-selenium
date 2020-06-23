package example

import khtml.annotations.Element
import khtml.annotations.Fragment
import khtml.context.FragmentContext
import khtml.element.Image
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
