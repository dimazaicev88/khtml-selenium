package example

import core.khtml.annotations.Element
import core.khtml.annotations.Fragment
import core.khtml.annotations.Param
import core.khtml.element.Link

@Fragment(".//header[contains(@class,'masthead')]")
interface Portfolio {

    @Element(".//a[@href='#portfolio-modal-#{numberPortfolio}']")
    fun protfolioLink(@Param("numberPortfolio") numberPortfolio: Int): Link
}
