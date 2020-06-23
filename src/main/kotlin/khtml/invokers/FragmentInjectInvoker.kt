package khtml.invokers

import khtml.annotations.Wait
import khtml.build.XpathBuilder
import khtml.conf.Configuration
import khtml.element.HtmlElement
import khtml.utils.WebDriverUtils.waitConditionFragment

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