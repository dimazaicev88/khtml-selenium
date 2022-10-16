package org.intsite.khtml.build

import org.intsite.khtml.conf.XpathItem


/**
 * Сборщик XPATH
 */
class XpathBuilder {

    companion object {

        fun buildXpath(xpathItem: List<XpathItem>): String {
            var resultXpath = ""
            for ((index, item) in xpathItem.withIndex()) {
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
                            tmpXpath.split("|").map { resultXpath + it.trim().removePrefix(".") }.toList()
                                .joinToString("|")
                        } else {
                            if (tmpXpath.isNotEmpty() && tmpXpath.first() != "/".toCharArray()[0]) {
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

        fun buildXpathWithLastPosition(xpathItem: List<XpathItem>, position: Int): String {
            return "(${buildXpath(xpathItem)})[$position]"
        }
    }
}