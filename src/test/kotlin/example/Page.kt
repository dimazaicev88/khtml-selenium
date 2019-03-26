package example

import core.khtml.annotations.*
import core.khtml.element.HtmlElement
import core.khtml.element.Link
import core.khtml.waits.WaitCondition

@Fragment(".//body")
interface AbstractFragment {

    @Fragment(".//div")
    fun someFragment(): SomeFragment

    @Element(".//a']")
    fun someLink(): Link
}

interface SomeFragment {

    fun def(): Boolean {
        return false
    }
}






