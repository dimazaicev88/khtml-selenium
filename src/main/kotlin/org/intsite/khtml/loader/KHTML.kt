package org.intsite.khtml.loader

import org.intsite.khtml.conf.Configuration
import org.intsite.khtml.ext.isFragment
import org.intsite.khtml.ext.isPage
import org.intsite.khtml.invokers.ProxyHandler
import org.intsite.khtml.utils.ReflectUtils.createProxy
import org.openqa.selenium.WebDriver

class KHTML {

    companion object {
        fun decorate(page: Any, driver: WebDriver) {
            val fields = page::class.java.declaredFields
            fields.forEach { _field ->
                if (
                    _field.type.isInterface && _field.type.isPage ||
                    _field.type.isFragment
                ) {
                    val configuration = Configuration(driver = driver, parentClass = _field.type)
                    val fieldValue = createProxy(_field.type, ProxyHandler(configuration))
                    configuration.proxyCache[_field.type] = fieldValue
                    _field.isAccessible = true
                    _field.set(page, fieldValue)
                }
            }
        }
    }
}


