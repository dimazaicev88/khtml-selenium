package example

import core.khtml.annotations.*
import core.khtml.element.HtmlElement
import core.khtml.element.Link


@Page
interface ElementInPage {

    @Element(".//div[@class='elementInPage']")
    fun elementInPage(): Link
}

@Page
interface FragmentInPage {

    @Fragment(".//div[@class='fragmentInPage']")
    fun fragmentInPage(): ResultFragment
}

interface ResultFragment {

    @InjectContext
    fun context(): HtmlElement
}

@Fragment(".//div[@class='contextFragmentContext']")
interface ContextInFragment {

    @InjectContext
    fun context(): HtmlElement
}

@Fragment(".//div[@class='FragmentInFragment']")
interface FragmentInFragment {

    @Fragment(".//div[@class='fragmentInFragment']")
    fun fragmentInFragment(): ResultFragment
}

interface InheritanceFragment : BaseFragmentLevel3 {

    @InjectContext
    fun context(): HtmlElement
}

@Fragment(".//div[@class='BaseFragmentLevel3']", inheritance = true)
interface BaseFragmentLevel3 : BaseFragmentLevel2 {

}

@Fragment(".//div[@class='BaseFragmentLevel2']", inheritance = true)
interface BaseFragmentLevel2 : BaseFragment {

}

@Fragment(".//div[@class='BaseFragment']", inheritance = true)
interface BaseFragment {

}

@Dump
@Fragment(".//div[@class='DumpFragment']")
interface DumpFragment {

    @Fragment(".//ddd")
    fun listFragments(): List<BaseFragment>

}

