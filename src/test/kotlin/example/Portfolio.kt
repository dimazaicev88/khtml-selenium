package example

import khtml.annotations.Element
import khtml.annotations.Fragment
import khtml.annotations.Param
import khtml.element.Link

@Fragment(".//header[contains(@class,'masthead')]")
interface Portfolio {

    @Element(".//a[@href='#portfolio-modal-#{numberPortfolio}']")
    fun protfolioLink(@Param("numberPortfolio") numberPortfolio: Int): Link
}
