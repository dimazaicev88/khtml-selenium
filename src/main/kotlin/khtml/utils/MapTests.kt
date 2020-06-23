package khtml.utils

import ext.jsonStrToObject
import ext.toFile
import ext.toJson
import org.openqa.selenium.WebDriver

class MapTests {
    private val maps = arrayListOf<Maps>()

    fun add(xpath: String, testName: String?, driver: WebDriver) {
//        val url = try {
//            driver.currentUrl
//        } catch (e: Exception) {
//            ""
//        }
//        if (testName != null) {
//            var map = this.loadMap()
//            if (map == null) {
//                map = MapTests()
//            }
//            val item = map.maps.firstOrNull {
//                it.testName == testName
//            }
//            if (item != null) {
//                if (!item.xpathInfo.containsKey(url)) {
//                    item.xpathInfo[url] = hashSetOf(xpath)
//                } else {
//                    val xpaths = item.xpathInfo[url]!!
//                    xpaths.add(xpath)
//                    item.xpathInfo[url] = xpaths
//                }
//            } else {
//                map.maps.add(Maps(hashMapOf(url to hashSetOf(xpath)), testName))
//            }
//            this.saveMap(map)
//        }
    }

    fun loadMap(): MapTests? {
        return "F:\\work\\officemag-ui-tests\\src\\test\\resources\\maps\\maps.json".toFile().readText().jsonStrToObject(MapTests::class.java)
    }

    fun saveMap(map: MapTests) {
        "F:\\work\\officemag-ui-tests\\src\\test\\resources\\maps\\maps.json".toFile().writeText(map.toJson())
    }
}

data class Maps(
        var xpathInfo: MutableMap<String, MutableSet<String>> = HashMap(),
        var testName: String = ""
)
