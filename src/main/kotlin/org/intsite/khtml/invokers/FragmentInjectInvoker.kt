package org.intsite.khtml.invokers

import org.intsite.khtml.annotations.Wait
import org.intsite.khtml.build.XpathBuilder
import org.intsite.khtml.conf.Configuration
import org.intsite.khtml.element.HtmlElement
import org.intsite.khtml.utils.MethodWrapper
import org.intsite.khtml.utils.WebDriverUtils.waitConditionFragment

class FragmentInjectInvoker : MethodInvoker {

    override fun invoke(proxy: Any, methodWrapper: MethodWrapper, config: Configuration): Any {
        if (config.xpathItems.size > 0) {
            config.xpathItems.last.position = config.instanceId
        }
        if (methodWrapper.method.isAnnotationPresent(Wait::class.java)) {
            waitConditionFragment(methodWrapper.method, config.driver, config.xpathItems)
        }
        val xpathResult = XpathBuilder.buildXpath(config.xpathItems)
        return HtmlElement(xpathResult, config.driver)
    }
}