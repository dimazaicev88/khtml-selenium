package khtml.example

import org.intsite.khtml.annotations.Element
import org.intsite.khtml.annotations.Fragment
import org.intsite.khtml.annotations.Param
import org.intsite.khtml.element.Link

@Fragment(".//header[contains(@class,'masthead')]")
interface Portfolio {

    @Element(".//a[@href='#portfolio-modal-#{numberPortfolio}']")
    fun protfolioLink(@Param("numberPortfolio") numberPortfolio: Int): Link
}
