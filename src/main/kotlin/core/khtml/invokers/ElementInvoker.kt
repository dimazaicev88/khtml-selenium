package core.khtml.invokers

import core.khtml.annotations.Element
import core.khtml.annotations.Fragment
import core.khtml.annotations.Wait
import core.khtml.build.XpathBuilder.Companion.buildXpath
import core.khtml.build.XpathBuilder.Companion.buildXpathWithLastPosition
import core.khtml.conf.Configuration
import core.khtml.conf.FullXpath
import core.khtml.element.HtmlElement
import core.khtml.ext.returnMethodType
import core.khtml.utils.ReflectUtils
import core.khtml.utils.ReflectUtils.createCustomElement
import core.khtml.utils.ReflectUtils.createHtmlElement
import core.khtml.utils.ReflectUtils.getMethodParams
import core.khtml.utils.ReflectUtils.isCustomElement
import core.khtml.utils.ReflectUtils.isCustomElementList
import core.khtml.utils.ReflectUtils.isHtmlElement
import core.khtml.utils.ReflectUtils.isHtmlElementList
import core.khtml.utils.ReflectUtils.replaceParams
import core.khtml.utils.SearchType
import core.khtml.utils.WebDriverUtils.searchWebElement
import core.khtml.utils.WebDriverUtils.waitConditionFragment
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.WebElement


class ElementInvoker : MethodInvoker {

    @Suppress("UNCHECKED_CAST")
    override fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): Any {
        val mapParams = getMethodParams(methodInfo.method, methodInfo.args)
        val template: String = methodInfo.method.getAnnotation(Element::class.java).xpath
        val xpath = replaceParams(template, mapParams)

        if (methodInfo.method.declaringClass.isAssignableFrom(config.parentClass)) {
            config.fullXpath.clear()
        }

        val fragmetxx = ReflectUtils.findFragmentXpath(methodInfo.method.declaringClass)

        if (methodInfo.method.declaringClass.isAnnotationPresent(Fragment::class.java)) {
            val fragmentXpath = methodInfo.method.declaringClass.getAnnotation(Fragment::class.java).xpath
            config.fullXpath.add(FullXpath(fragmentXpath))
        }

        if (config.fullXpath.none { it.method == methodInfo.method }) {
            config.fullXpath.add(FullXpath(xpath, method = methodInfo.method))
        }

        if (methodInfo.method.isAnnotationPresent(Wait::class.java)) {
            waitConditionFragment(methodInfo.method, config.driver, config.fullXpath)
        }
        when {
            isCustomElement(methodInfo.method.returnType) -> {
                return createCustomElement(
                    methodInfo.method.returnType,
                    buildXpath(config.fullXpath),
                    config.driver
                )
            }
            isHtmlElement(methodInfo.method.returnType) -> {
                return createHtmlElement(
                    methodInfo.method.returnType as Class<HtmlElement>,
                    buildXpath(config.fullXpath),
                    config.driver
                )
            }
            isCustomElementList(methodInfo.method) -> {
                val elements = searchWebElement(
                    config.driver, buildXpath(config.fullXpath), SearchType.ALL
                ) as List<WebElement>
                return (0 until elements.size).map {
                    createCustomElement(
                        methodInfo.method.returnMethodType!!,
                        buildXpathWithLastPosition(config.fullXpath, it + 1),
                        config.driver
                    )
                }.toList()
            }
            isHtmlElementList(methodInfo.method) -> {
                val elements = searchWebElement(
                    config.driver, buildXpath(config.fullXpath), SearchType.ALL
                ) as List<WebElement>
                return (0 until elements.size).map {
                    createHtmlElement(
                        methodInfo.method.returnMethodType as Class<HtmlElement>,
                        buildXpathWithLastPosition(config.fullXpath, it + 1),
                        config.driver
                    )
                }.toList()
            }
            else -> {
                throw WebDriverException("Unknown type")
            }
        }
    }
}