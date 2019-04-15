package core.khtml.invokers

import core.khtml.annotations.Fragment
import core.khtml.annotations.Wait
import core.khtml.build.XpathBuilder
import core.khtml.conf.Configuration
import core.khtml.conf.FullXpath
import core.khtml.utils.ReflectUtils
import core.khtml.utils.ReflectUtils.createProxy
import core.khtml.utils.ReflectUtils.findAnnotation
import core.khtml.utils.ReflectUtils.findFragmentTemplate
import core.khtml.utils.ReflectUtils.fullXpathFromClass
import core.khtml.utils.ReflectUtils.getDumpInfo
import core.khtml.utils.ReflectUtils.getMethodParams
import core.khtml.utils.ReflectUtils.replaceParams
import core.khtml.utils.WebDriverUtils.dump
import core.khtml.utils.WebDriverUtils.safeOperation
import core.khtml.utils.WebDriverUtils.waitConditionFragment
import org.openqa.selenium.By

class FragmentInvoker : MethodInvoker {

    @Suppress("UNCHECKED_CAST")
    override fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): Any {
        val mapParams = getMethodParams(methodInfo.method, methodInfo.args)
        val template = findFragmentTemplate(methodInfo.method)
        val xpath = replaceParams(template, mapParams)

        val dumpInfo = getDumpInfo(methodInfo.method.declaringClass)

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

        config.fullXpath.add(FullXpath(xpath))
        if (dumpInfo != null) {
            val resultXpath = XpathBuilder.buildXpath(config.fullXpath)
            dump(resultXpath, driver = config.driver, dumpInfo = dumpInfo) {
                safeOperation { config.driver.findElement(By.xpath(resultXpath)) }
            }
        }
        if (methodInfo.method.isAnnotationPresent(Wait::class.java)) {
            waitConditionFragment(methodInfo.method, config.driver, config.fullXpath)
        }
        return config.proxyCache.computeIfAbsent(methodInfo.method.returnType) {
            createProxy(methodInfo.method.returnType, ProxyHandler(config))
        }
    }
}