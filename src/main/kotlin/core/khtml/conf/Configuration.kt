package core.khtml.conf

import org.openqa.selenium.WebDriver
import java.util.*

data class Configuration(
    var fullXpath: LinkedList<FullXpath> = LinkedList(),
    var driver: WebDriver,
    var proxyCache: HashMap<Class<*>, Any> = hashMapOf(),
    var target: Any? = null,
    var instanceId: Int = 0,
    val parentClass: Class<*>
)

data class FullXpath(
    val xpath: String,
    var position: Int = 0
)