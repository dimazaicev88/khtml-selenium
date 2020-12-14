package khtml.example

import org.intsite.khtml.annotations.Element
import org.intsite.khtml.annotations.Fragment
import org.intsite.khtml.context.FragmentContext
import org.intsite.khtml.element.Image
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
