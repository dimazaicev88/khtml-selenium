package org.intsite.khtml.loader

import org.intsite.khtml.annotations.Fragment
import org.intsite.khtml.annotations.Page
import org.intsite.khtml.conf.Configuration
import org.intsite.khtml.invokers.ProxyHandler
import org.intsite.khtml.utils.ReflectUtils.createProxy
import org.intsite.khtml.utils.ReflectUtils.findAnnotation
import org.openqa.selenium.WebDriver

class KHTML {

    companion object {
        fun decorate(page: Any, driver: WebDriver, testName: String? = null) {
            val fields = page::class.java.declaredFields
            fields.forEach { _field ->
                if (_field.type.isInterface && (findAnnotation(_field.type, Page::class.java)
                                || findAnnotation(_field.type, Fragment::class.java))
                ) {
                    val configuration = Configuration(driver = driver, parentClass = _field.type, testName = testName)
                    val fieldValue = createProxy(_field.type, ProxyHandler(configuration))
                    configuration.proxyCache[_field.type] = fieldValue
                    _field.isAccessible = true
                    _field.set(page, fieldValue)
                }
            }
        }
    }
}


