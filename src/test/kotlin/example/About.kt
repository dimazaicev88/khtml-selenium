package example

import org.intsite.khtml.annotations.Element
import org.intsite.khtml.annotations.Fragment
import org.intsite.khtml.context.FragmentContext
import org.intsite.khtml.element.Button

@Fragment(".//section[@id='about']")
interface About : FragmentContext {

    @Element(".//a[@id='btn_download']")
    fun buttonDownload(): Button
}
