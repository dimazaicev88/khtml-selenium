package core.khtml.loader

import core.khtml.annotations.Fragment
import core.khtml.annotations.Page
import core.khtml.conf.Configuration
import core.khtml.invokers.ProxyHandler
import core.khtml.utils.ReflectUtils.createProxy
import core.khtml.utils.ReflectUtils.isFindAnnotation
import org.openqa.selenium.WebDriver

class KHTML {

    companion object {
        fun decorate(page: Any, driver: WebDriver) {
            val fields = page::class.java.declaredFields
            fields.forEach { _field ->
                if (_field.type.isInterface && (isFindAnnotation(_field.type, Page::class.java)
                            || isFindAnnotation(_field.type, Fragment::class.java))
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


