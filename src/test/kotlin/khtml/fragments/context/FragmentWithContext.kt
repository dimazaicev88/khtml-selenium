package khtml.fragments.context

import org.intsite.khtml.annotations.Fragment
import org.intsite.khtml.annotations.InjectContext
import org.intsite.khtml.annotations.Page
import org.intsite.khtml.element.HtmlElement

@Fragment("")
interface EmptyFragmentWithContext {

    @InjectContext
    fun context(): HtmlElement
}

@Page
interface PageWithContext {

    @InjectContext
    fun context(): HtmlElement
}

@Fragment(".//div[@id='TestFragment']")
interface FragmentWithContext {

    @InjectContext
    fun context(): HtmlElement

    fun fragmentTwo(): FragmentTwo

    fun listFragment():List<ItemFragment>
}

@Fragment(".//span[@class='FragmentItem']")
interface ItemFragment {

    @InjectContext
    fun context(): HtmlElement

}

@Fragment("//div[@id='TestFragment2']")
interface FragmentTwo {

    @InjectContext
    fun context(): HtmlElement
}
