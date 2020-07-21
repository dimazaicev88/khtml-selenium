package org.intsite.khtml.utils

import org.openqa.selenium.WebDriver

class MapTests {
    private val maps = arrayListOf<Maps>()

    fun add(xpath: String, testName: String?, driver: WebDriver) {

    }
}

data class Maps(
        var xpathInfo: MutableMap<String, MutableSet<String>> = HashMap(),
        var testName: String = ""
)
