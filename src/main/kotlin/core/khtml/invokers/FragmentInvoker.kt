package core.khtml.invokers

import core.khtml.annotations.Fragment
import core.khtml.annotations.Wait
import core.khtml.conf.Configuration
import core.khtml.conf.FullXpath
import core.khtml.utils.ReflectUtils
import core.khtml.utils.ReflectUtils.createProxy
import core.khtml.utils.ReflectUtils.findAnnotation
import core.khtml.utils.ReflectUtils.findFragmentTemplate
import core.khtml.utils.ReflectUtils.fullXpathFromClass
import core.khtml.utils.ReflectUtils.getMethodParams
import core.khtml.utils.ReflectUtils.replaceParams
import core.khtml.utils.WebDriverUtils.waitConditionFragment

class FragmentInvoker : MethodInvoker {

    @Suppress("UNCHECKED_CAST")
    override fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): Any {
        val mapParams = getMethodParams(methodInfo.method, methodInfo.args)
        val template = findFragmentTemplate(methodInfo.method)
        val xpath = replaceParams(template, mapParams)

        if (methodInfo.method.declaringClass.isAssignableFrom(config.parentClass)) {
            config.fullXpath.clear()
        }
        if (config.fullXpath.size > 0) {
            config.fullXpath.last.position = config.instanceId
        }
        config.target = methodInfo.method.declaringClass

        if (findAnnotation(methodInfo.method.declaringClass, Fragment::class.java))
            config.fullXpath.addAll(fullXpathFromClass(methodInfo.method.declaringClass!!))

        if (xpath.isNotEmpty())
            config.fullXpath.add(FullXpath(xpath))

        if (methodInfo.method.isAnnotationPresent(Wait::class.java)) {
            waitConditionFragment(methodInfo.method, config.driver, config.fullXpath)
        }
        return config.proxyCache.computeIfAbsent(methodInfo.method.returnType) {
            createProxy(methodInfo.method.returnType, ProxyHandler(config))
        }
    }
}