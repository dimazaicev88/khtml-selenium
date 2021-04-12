package fragments.parentxpath

import org.intsite.khtml.annotations.Element
import org.intsite.khtml.annotations.Fragment
import org.intsite.khtml.annotations.NotUseParentXpath
import org.intsite.khtml.element.Button

@Fragment(".//div[@id='parent']")
interface ParentFragment {

    @NotUseParentXpath
    @Fragment(".//div[@class='childFragment']")
    fun childFragment(): ChildFragment

    @NotUseParentXpath
    fun childFragment2(): ChildFragment2
}

@Fragment(".//div[@class='childFragment2']")
interface ChildFragment2 {


    @Element(".//div[@id='btn']")
    fun someButton2(): Button

    @NotUseParentXpath
    @Element(".//div[@id='btn']")
    fun someButton3(): Button
}

interface ChildFragment {

    @Element(".//div[@id='btn']")
    fun someButton(): Button
}
