package core.khtml.build

import core.khtml.conf.FullXpath

/**
 * Сборщик XPATH
 */
class XpathBuilder {

    companion object {

        fun buildXpath(fullXpath: List<FullXpath>): String {
            var resultXpath = ""
            for ((index, item) in fullXpath.withIndex()) {
                if (index == 0) {
                    resultXpath += if (item.position > 0) {
                        ("(${item.xpath})[${item.position + 1}]")
                    } else {
                        item.xpath
                    }
                } else {
                    val tmpXpath = item.xpath.removePrefix(".")
                    resultXpath = if (item.position > 0) {
                        "($resultXpath$tmpXpath)[${item.position + 1}]"
                    } else {
                        "$resultXpath$tmpXpath"
                    }
                }
            }
            return resultXpath
        }

        fun buildXpathWithLastPosition(fullXpath: List<FullXpath>, position: Int): String {
            return "(${buildXpath(fullXpath)})[$position]"
        }
    }
}