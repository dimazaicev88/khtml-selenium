package ui.dump

import core.khtml.annotations.Dump
import core.khtml.annotations.Element
import core.khtml.annotations.Fragment
import core.khtml.element.HtmlElement

@Fragment(".//html")
interface DumpFragment {

    @Fragment(".//body")
    fun innerFragment(): ResultFragment
}

interface ResultFragment {

    @Element(".//span")
    fun elem(): HtmlElement
}
