package fragments.fragments

import org.intsite.khtml.annotations.Element
import org.intsite.khtml.annotations.Fragment
import org.intsite.khtml.annotations.InjectContext
import org.intsite.khtml.annotations.Page
import org.intsite.khtml.element.HtmlElement
import org.intsite.khtml.element.Text

@Page
interface BasePage {

    @Fragment(".//div[@class='first']")
    fun firstFragment(): FirstFragment
}

interface FirstFragment {

    @InjectContext
    fun context(): HtmlElement

    @Fragment(".//div[@class='second']")
    fun secondFragment(): SecondFragment

    @Fragment(".//div[@class='elements']")
    fun findAllElements(): List<FragmentElements>
}

interface FragmentElements {

    @Element(".//div[@class='value']")
    fun value(): Text
}


interface SecondFragment {

    @InjectContext
    fun context(): HtmlElement

    @Fragment(".//div[@class='elements2']")
    fun findAllElements(): List<FragmentElements2>
}

interface FragmentElements2 {
    @InjectContext
    fun context(): HtmlElement
}
