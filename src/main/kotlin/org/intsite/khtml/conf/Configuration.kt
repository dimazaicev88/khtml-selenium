package org.intsite.khtml.conf

import org.openqa.selenium.WebDriver
import java.util.*

data class Configuration(
        var xpathItems: LinkedList<XpathItem> = LinkedList(),
        var driver: WebDriver,
        var proxyCache: HashMap<Class<*>, Any> = hashMapOf(),
        var target: Any? = null,
        var instanceId: Int = 0,
        val parentClass: Class<*>
)

data class XpathItem(
        val xpath: String,
        var clazz: Class<*>,
        var position: Int = 0
)