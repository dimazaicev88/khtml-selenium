package khtml.build

import khtml.conf.FullXpath

/**
 * Сборщик XPATH
 */
class XpathBuilder {

    companion object {

        fun buildXpath(fullXpath: List<FullXpath>): String {
            var resultXpath = ""
            for ((index, item) in fullXpath.withIndex()) {
                if (index == 0) {
                    resultXpath += when {
                        item.position > 0 -> {
                            ("(${item.xpath})[${item.position + 1}]")
                        }
                        item.xpath.contains("|") -> {
                            "(${item.xpath})"
                        }
                        else -> {
                            item.xpath
                        }
                    }
                } else {
                    val tmpXpath = item.xpath.trim().removePrefix(".")
                    resultXpath = if (item.position > 0) {
                        "($resultXpath$tmpXpath)[${item.position + 1}]"
                    } else {
                        if (tmpXpath.contains("|")) {
                            tmpXpath.split("|").map { resultXpath + it.trim().removePrefix(".") }.toList().joinToString("|")
                        } else {
                            if (tmpXpath.first() != "/".toCharArray()[0]) {
                                "$resultXpath//$tmpXpath"
                            } else {
                                "$resultXpath$tmpXpath"
                            }
                        }
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