package example

import khtml.annotations.Element
import khtml.annotations.Fragment
import khtml.context.FragmentContext
import khtml.element.Button

@Fragment(".//section[@id='about']")
interface About : FragmentContext {

    @Element(".//a[@id='btn_download']")
    fun buttonDownload(): Button
}
