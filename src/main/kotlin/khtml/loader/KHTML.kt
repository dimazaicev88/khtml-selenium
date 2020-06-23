package khtml.loader

import khtml.annotations.Fragment
import khtml.annotations.Page
import khtml.conf.Configuration
import khtml.invokers.ProxyHandler
import khtml.utils.ReflectUtils.createProxy
import khtml.utils.ReflectUtils.findAnnotation
import org.openqa.selenium.WebDriver

class KHTML {

    companion object {
        fun decorate(page: Any, driver: WebDriver) {
            val fields = page::class.java.declaredFields
            fields.forEach { _field ->
                if (_field.type.isInterface && (findAnnotation(_field.type, Page::class.java)
                            || findAnnotation(_field.type, Fragment::class.java))
                ) {
                    val configuration =
                        Configuration(driver = driver, parentClass = _field.type)
                    val fieldValue = createProxy(_field.type,
                        ProxyHandler(configuration)
                    )
                    configuration.proxyCache[_field.type] = fieldValue
                    _field.isAccessible = true
                    _field.set(page, fieldValue)
                }
            }
        }
    }
}


