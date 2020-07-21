package org.intsite.khtml.invokers

import org.intsite.khtml.annotations.Fragment
import org.intsite.khtml.annotations.Wait
import org.intsite.khtml.build.XpathBuilder.Companion.buildXpath
import org.intsite.khtml.conf.Configuration
import org.intsite.khtml.conf.FullXpath
import org.intsite.khtml.element.HtmlElement
import org.intsite.khtml.utils.WebDriverUtils.waitConditionFragment
import java.util.*

class ContextInjectInvoker : MethodInvoker {

    override fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): Any {
        val tmpFullXpath = config.fullXpath.clone() as LinkedList<FullXpath>

        if (config.fullXpath.size > 0) {
            config.fullXpath.last.position = config.instanceId
        }

        val clazz = methodInfo.method.declaringClass
        if (clazz.isAnnotationPresent(Fragment::class.java)) {
            tmpFullXpath.add(FullXpath(clazz.getAnnotation(Fragment::class.java).xpath, methodInfo.method.declaringClass))
        }

        if (methodInfo.method.isAnnotationPresent(Wait::class.java)) {
            waitConditionFragment(methodInfo.method, config.driver, tmpFullXpath)
        }

        return HtmlElement(buildXpath(tmpFullXpath), config.driver)
    }
}