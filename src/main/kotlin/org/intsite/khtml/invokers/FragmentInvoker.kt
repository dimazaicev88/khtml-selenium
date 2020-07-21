package org.intsite.khtml.invokers

import org.intsite.khtml.annotations.Fragment
import org.intsite.khtml.annotations.Wait
import org.intsite.khtml.conf.Configuration
import org.intsite.khtml.conf.FullXpath
import org.intsite.khtml.utils.ReflectUtils.createProxy
import org.intsite.khtml.utils.ReflectUtils.findAnnotation
import org.intsite.khtml.utils.ReflectUtils.findFragmentTemplate
import org.intsite.khtml.utils.ReflectUtils.fullXpathFromClass
import org.intsite.khtml.utils.ReflectUtils.getMethodParams
import org.intsite.khtml.utils.ReflectUtils.replaceParams
import org.intsite.khtml.utils.WebDriverUtils.waitConditionFragment

class FragmentInvoker : MethodInvoker {

    @Suppress("UNCHECKED_CAST")
    override fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): Any {
        val mapParams = getMethodParams(methodInfo.method, methodInfo.args)
        val template = findFragmentTemplate(methodInfo.method)
        val xpath = replaceParams(template, mapParams)

        if (methodInfo.method.declaringClass.isAssignableFrom(config.parentClass)) {
            config.fullXpath.clear()
        }

        if (config.fullXpath.size > 0 && config.fullXpath.last.clazz == methodInfo.method.declaringClass) {
            config.fullXpath.removeLast()
        }

        if (config.fullXpath.size > 0) {
            config.fullXpath.last.position = config.instanceId
        }

        config.target = methodInfo.method.declaringClass

        if (findAnnotation(methodInfo.method.declaringClass, Fragment::class.java))
            config.fullXpath.addAll(fullXpathFromClass(methodInfo.method.declaringClass))

        if (xpath.isNotEmpty())
            config.fullXpath.add(FullXpath(xpath, clazz = methodInfo.method.declaringClass))

        if (methodInfo.method.isAnnotationPresent(Wait::class.java)) {
            waitConditionFragment(methodInfo.method, config.driver, config.fullXpath)
        }

        return config.proxyCache.computeIfAbsent(methodInfo.method.returnType) {
            createProxy(methodInfo.method.returnType, ProxyHandler(config))
        }
    }
}