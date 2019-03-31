package example

import core.khtml.annotations.*
import core.khtml.element.Link


interface AbstractFragment : F1, F2 {

    @Fragment(".//div")
    fun someFragment(): F1

    @Element(".//a']")
    fun someLink(): Link
}

@Fragment(".//f1")
interface F1 {

    fun def(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

@Fragment(".//f1")
interface F2 {

}






