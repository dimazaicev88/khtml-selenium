package core.khtml.invokers

import core.khtml.annotations.Wait
import core.khtml.build.XpathBuilder.Companion.buildXpath
import core.khtml.conf.Configuration
import core.khtml.element.HtmlElement
import core.khtml.utils.WebDriverUtils.waitConditionFragment

class FragmentInjectInvoker : MethodInvoker {

    override fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): Any {
        if (config.fullXpath.size > 0) {
            config.fullXpath.last.position = config.instanceId
        }
        if (methodInfo.method.isAnnotationPresent(Wait::class.java)) {
            waitConditionFragment(methodInfo.method, config.driver, config.fullXpath)
        }
        val xpathResult = buildXpath(config.fullXpath)
        return HtmlElement(xpathResult, config.driver)
    }
}