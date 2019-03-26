package example

import core.khtml.annotations.Element
import core.khtml.annotations.Fragment
import core.khtml.annotations.Param
import core.khtml.context.FragmentContext
import core.khtml.element.Link

@Fragment(".//nav[@id='mainNav']")
interface MainNav : FragmentContext {

    @Element(".//a[contains(@class,'nav-link') and text()='#{linkText}']")
    fun linkNav(@Param("linkText") linkText: String): Link

}
