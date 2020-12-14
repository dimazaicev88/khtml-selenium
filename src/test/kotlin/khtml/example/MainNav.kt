package khtml.example

import org.intsite.khtml.annotations.Element
import org.intsite.khtml.annotations.Fragment
import org.intsite.khtml.annotations.Param
import org.intsite.khtml.context.FragmentContext
import org.intsite.khtml.element.Link

@Fragment(".//nav[@id='mainNav']")
interface MainNav : FragmentContext {

    @Element(".//a[contains(@class,'nav-link') and text()='#{linkText}']")
    fun linkNav(@Param("linkText") linkText: String): Link

}
