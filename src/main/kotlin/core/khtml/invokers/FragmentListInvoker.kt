package core.khtml.invokers

import core.khtml.annotations.Fragment
import core.khtml.annotations.Page
import core.khtml.annotations.Wait
import core.khtml.build.XpathBuilder.Companion.buildXpath
import core.khtml.conf.Configuration
import core.khtml.conf.FullXpath
import core.khtml.ext.returnMethodType
import core.khtml.utils.ReflectUtils
import core.khtml.utils.ReflectUtils.createProxy
import core.khtml.utils.ReflectUtils.findFragmentTemplate
import core.khtml.utils.ReflectUtils.getMethodParams
import core.khtml.utils.ReflectUtils.replaceParams
import core.khtml.utils.SearchType
import core.khtml.utils.WebDriverUtils.searchWebElement
import core.khtml.utils.WebDriverUtils.waitConditionFragment
import org.openqa.selenium.WebElement

class FragmentListInvoker : MethodInvoker {

    @Suppress("UNCHECKED_CAST")
    override fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): Any? {
        val mapParams = getMethodParams(methodInfo.method, methodInfo.args)
        val template = findFragmentTemplate(methodInfo.method)
        val xpath = replaceParams(template, mapParams)

        if (methodInfo.method.declaringClass.isAnnotationPresent(Page::class.java) ||
            methodInfo.method.declaringClass.isAssignableFrom(config.parentClass)) {
            config.fullXpath.clear()
        }
        if (config.fullXpath.size > 0) {
            config.fullXpath.last.position = config.instanceId
        }
        if (ReflectUtils.findAnnotation(methodInfo.method.declaringClass, Fragment::class.java)) {
            config.fullXpath.addAll(ReflectUtils.fullXpathFromClass(methodInfo.method.declaringClass))
        }
        config.fullXpath.add(FullXpath(xpath))
        config.instanceId = 0
        if (methodInfo.method.isAnnotationPresent(Wait::class.java)) {
            waitConditionFragment(methodInfo.method, config.driver, config.fullXpath)
        }
        val elements = searchWebElement(
            config.driver, buildXpath(config.fullXpath), SearchType.ALL
        ) as List<WebElement>
        val listElements = (0 until elements.size).map {
            val typeGeneric = methodInfo.method.returnMethodType
                ?: throw RuntimeException("Undefined generic type")
            createProxy(typeGeneric, ProxyHandler(config, instanceId = it))
        }.toList()
        config.target = listElements
        config.proxyCache.computeIfAbsent(methodInfo.method.returnType) {
            createProxy(methodInfo.method.returnType, ProxyHandler(config))
        }
        return config.proxyCache[methodInfo.method.returnType]
    }
}