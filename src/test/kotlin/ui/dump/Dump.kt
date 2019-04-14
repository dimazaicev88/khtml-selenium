package ui.dump

import core.khtml.annotations.Dump
import core.khtml.annotations.Fragment

@Dump
@Fragment(".//div[@class='dump']")
interface DumpFragment {

    @Fragment(".//innerFragment")
    fun innerFragment(): ResultFragment
}

interface ResultFragment {

}
