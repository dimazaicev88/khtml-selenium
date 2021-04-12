package org.intsite.khtml.invokers

import org.intsite.khtml.annotations.Fragment
import org.intsite.khtml.annotations.Wait
import org.intsite.khtml.build.XpathBuilder.Companion.buildXpath
import org.intsite.khtml.conf.Configuration
import org.intsite.khtml.conf.XpathItem
import org.intsite.khtml.element.HtmlElement
import org.intsite.khtml.utils.MethodWrapper
import org.intsite.khtml.utils.WebDriverUtils.waitConditionFragment
import java.util.*

class ContextInjectInvoker : MethodInvoker {

    override fun invoke(proxy: Any, methodWrapper: MethodWrapper, config: Configuration): Any {
        val tmpFullXpath = config.xpathItems.clone() as LinkedList<XpathItem>

        if (config.xpathItems.size > 0) {
            config.xpathItems.last.position = config.instanceId
        }

        val clazz = methodWrapper.method.declaringClass
        if (
                clazz.isAnnotationPresent(Fragment::class.java) && (tmpFullXpath.isEmpty() ||
                        (tmpFullXpath.isNotEmpty() && clazz != tmpFullXpath.last.clazz))
        ) {
            tmpFullXpath.add(
                    XpathItem(
                            clazz.getAnnotation(Fragment::class.java).xpath,
                            methodWrapper.method.declaringClass
                    )
            )
        }

        if (methodWrapper.method.isAnnotationPresent(Wait::class.java)) {
            waitConditionFragment(methodWrapper.method, config.driver, tmpFullXpath)
        }

        return HtmlElement(buildXpath(tmpFullXpath), config.driver)
    }
}