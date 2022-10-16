package example

import org.intsite.khtml.annotations.Element
import org.intsite.khtml.annotations.Fragment
import org.intsite.khtml.element.Link

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






