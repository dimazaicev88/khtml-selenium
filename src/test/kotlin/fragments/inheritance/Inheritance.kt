package fragments.inheritance

import org.intsite.khtml.annotations.Element
import org.intsite.khtml.annotations.Fragment
import org.intsite.khtml.element.Button

@Fragment(".//div[@id='FragmentFirst']")
interface FragmentFirst : FragmentSecond, FragmentThird {

    @Element(".//button[@id='btn']")
    fun elem(): Button
}

@Fragment(".//div[@id='FragmentSecond']", inheritance = true)
interface FragmentSecond

@Fragment(".//div[@id='FragmentSecond']")
interface FragmentThird