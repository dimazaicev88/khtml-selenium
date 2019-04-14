package core.khtml.invokers

import core.khtml.annotations.Page
import core.khtml.annotations.Wait
import core.khtml.build.XpathBuilder.Companion.buildXpath
import core.khtml.conf.Configuration
import core.khtml.conf.FullXpath
import core.khtml.ext.returnMethodType
import core.khtml.utils.ReflectUtils.createProxy
import core.khtml.utils.ReflectUtils.findFragmentTemplate
import core.khtml.utils.ReflectUtils.getDumpInfo

import core.khtml.utils.ReflectUtils.getMethodParams
import core.khtml.utils.ReflectUtils.replaceParams
import core.khtml.utils.WebDriverUtils.dump
import core.khtml.utils.WebDriverUtils.safeOperation
import core.khtml.utils.WebDriverUtils.waitConditionFragment
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import java.util.*

class FragmentListInvoker : MethodInvoker {

    @Suppress("UNCHECKED_CAST")
    override fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): Any? {
        val mapParams = getMethodParams(methodInfo.method, methodInfo.args)
        val template = findFragmentTemplate(methodInfo.method)
        val xpath = replaceParams(template, mapParams)
        val dumpInfo = getDumpInfo(methodInfo.method.declaringClass)

        //Сброс xpath, если вызов метода из класса Page
        if (methodInfo.method.declaringClass.isAnnotationPresent(Page::class.java)) {
            config.fullXpath.clear()
        }

        //Установка позиции элемента
        if (config.fullXpath.size > 0) {
            config.fullXpath.last.position = config.instanceId
        }
        config.fullXpath.add(FullXpath(xpath))
        config.instanceId = 0
        if (methodInfo.method.isAnnotationPresent(Wait::class.java)) {
            waitConditionFragment(methodInfo.method, config.driver, config.fullXpath)
        }
        val elements = if (dumpInfo != null) {
            val resultXpath = buildXpath(config.fullXpath)
            dump(resultXpath, driver = config.driver, dumpInfo = dumpInfo) {
                safeOperation {
                    config.driver.findElement(By.xpath(resultXpath))
                    config.driver.findElements(By.xpath(resultXpath))
                }
            } as List<WebElement>
        } else {
            safeOperation {
                config.driver.findElements(By.xpath(buildXpath(config.fullXpath)))
            } as List<WebElement>
        }

        val listElements = (0 until elements.size).map {
            val typeGeneric = methodInfo.method.returnMethodType
                ?: throw RuntimeException("Undefined generic type")
            createProxy(typeGeneric, ProxyHandler(config, instanceId = it))
        }.toMutableList()
        if (listElements.size == 1)
            listElements.add(null)
        config.target = listElements
        config.proxyCache.computeIfAbsent(methodInfo.method.returnType) {
            createProxy(methodInfo.method.returnType, ProxyHandler(config))
        }
        return config.proxyCache[methodInfo.method.returnType]
    }
}