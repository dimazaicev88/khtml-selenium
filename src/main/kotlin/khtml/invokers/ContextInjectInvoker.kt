package khtml.invokers

import khtml.annotations.Fragment
import khtml.annotations.Wait
import khtml.build.XpathBuilder.Companion.buildXpath
import khtml.conf.Configuration
import khtml.conf.FullXpath
import khtml.element.HtmlElement
import khtml.utils.WebDriverUtils.waitConditionFragment
import java.util.*

class ContextInjectInvoker : MethodInvoker {

    override fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): Any {
        val tmpFullXpath = config.fullXpath.clone() as LinkedList<FullXpath>

        if (config.fullXpath.size > 0) {
            config.fullXpath.last.position = config.instanceId
        }

        val clazz = methodInfo.method.declaringClass
        if (clazz.isAnnotationPresent(Fragment::class.java)) {
            tmpFullXpath.add(
                FullXpath(
                    clazz.getAnnotation(Fragment::class.java).xpath,
                    methodInfo.method.declaringClass
                )
            )
        }

        if (methodInfo.method.isAnnotationPresent(Wait::class.java)) {
            waitConditionFragment(methodInfo.method, config.driver, tmpFullXpath)
        }

        return HtmlElement(buildXpath(tmpFullXpath), config.driver)
    }
}