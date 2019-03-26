package example

import core.khtml.annotations.Element
import core.khtml.annotations.Fragment
import core.khtml.context.FragmentContext
import core.khtml.element.Button

@Fragment(".//section[@id='about']")
interface About : FragmentContext {

    @Element(".//a[@id='btn_download']")
    fun buttonDownload(): Button
}
