package example

import khtml.annotations.Element
import khtml.annotations.Fragment
import khtml.element.Link

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






