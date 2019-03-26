package example

import core.khtml.annotations.*
import core.khtml.element.HtmlElement
import core.khtml.element.Link
import core.khtml.waits.WaitCondition

interface AbstractPage : BasePage

@Page
interface BasePage


@Fragment(".//body")
interface AbstractFragment {

    @Wait(condition = WaitCondition.ENABLED)
    @Fragment(".//div[text()='#{blockName}']")
    fun someFragment(@Param("blockName") blockName: String): SomeFragment

    @Wait(condition = WaitCondition.TO_CLICKABLE)
    @Element(".//a[text()='#{linkName}']")
    fun someLink(@Param("linkName") linkName: String): Link
}

@Fragment(".//body")
interface SomeFragment {

    @InjectContext
    fun context(): HtmlElement

    fun printSource(xpath: String) {
        println(context().source)
    }
}




