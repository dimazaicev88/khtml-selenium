package example

import core.khtml.annotations.*
import core.khtml.element.Link


@Page
interface AbstractPage {

    @Element(".//div[@class='moving']")
    fun box(): Link
}