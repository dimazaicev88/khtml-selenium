package khtml.conf

import org.openqa.selenium.WebDriver
import java.util.*

data class Configuration(
        var fullXpath: LinkedList<FullXpath> = LinkedList(),
        var driver: WebDriver,
        var proxyCache: HashMap<Class<*>, Any> = hashMapOf(),
        var target: Any? = null,
        var instanceId: Int = 0,
        val parentClass: Class<*>,
        val testName: String? = null
)

data class FullXpath(
        val xpath: String,
        var clazz: Class<*>,
        var position: Int = 0
)