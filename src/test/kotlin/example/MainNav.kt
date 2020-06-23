package example

import khtml.annotations.Element
import khtml.annotations.Fragment
import khtml.annotations.Param
import khtml.context.FragmentContext
import khtml.element.Link

@Fragment(".//nav[@id='mainNav']")
interface MainNav : FragmentContext {

    @Element(".//a[contains(@class,'nav-link') and text()='#{linkText}']")
    fun linkNav(@Param("linkText") linkText: String): Link

}
