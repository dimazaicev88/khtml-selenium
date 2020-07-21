package org.intsite.khtml.invokers

import org.intsite.khtml.annotations.Wait
import org.intsite.khtml.build.XpathBuilder
import org.intsite.khtml.conf.Configuration
import org.intsite.khtml.element.HtmlElement
import org.intsite.khtml.utils.WebDriverUtils.waitConditionFragment

class FragmentInjectInvoker : MethodInvoker {

    override fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): Any {
        if (config.fullXpath.size > 0) {
            config.fullXpath.last.position = config.instanceId
        }
        if (methodInfo.method.isAnnotationPresent(Wait::class.java)) {
            waitConditionFragment(methodInfo.method, config.driver, config.fullXpath)
        }
        val xpathResult = XpathBuilder.buildXpath(config.fullXpath)
        return HtmlElement(xpathResult, config.driver)
    }
}