package khtml.invokers

import khtml.annotations.Element
import khtml.annotations.Fragment
import khtml.build.XpathBuilder.Companion.buildXpath
import khtml.build.XpathBuilder.Companion.buildXpathWithLastPosition
import khtml.conf.Configuration
import khtml.conf.FullXpath
import khtml.ext.returnMethodType
import khtml.utils.ReflectUtils.createCustomElement
import khtml.utils.ReflectUtils.createHtmlElement
import khtml.utils.ReflectUtils.findAnnotation
import khtml.utils.ReflectUtils.fullXpathFromClass
import khtml.utils.ReflectUtils.getMethodParams
import khtml.utils.ReflectUtils.isCustomElement
import khtml.utils.ReflectUtils.isCustomElementList
import khtml.utils.ReflectUtils.isHtmlElement
import khtml.utils.ReflectUtils.isHtmlElementList
import khtml.utils.ReflectUtils.replaceParams
import khtml.utils.WebDriverUtils.safeOperation
import khtml.utils.WebDriverUtils.waitConditionFragment
import org.openqa.selenium.By
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.WebElement
import java.util.*


class ElementInvoker : MethodInvoker {

    @Suppress("UNCHECKED_CAST")
    override fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): Any {
        val mapParams = getMethodParams(methodInfo.method, methodInfo.args)
        val template: String = methodInfo.method.getAnnotation(Element::class.java).xpath
        val xpath = replaceParams(template, mapParams)

        if (config.fullXpath.size > 0 && config.fullXpath.last.clazz == methodInfo.method.declaringClass) {
            config.fullXpath.removeLast()
        }

        val tmpFullXpath = config.fullXpath.clone() as LinkedList<FullXpath>

        if (methodInfo.method.declaringClass.isAssignableFrom(config.parentClass)) {
            tmpFullXpath.clear()
        }

        if (tmpFullXpath.size > 0) {
            tmpFullXpath.last.position = config.instanceId
        }

        if (findAnnotation(methodInfo.method.declaringClass, Fragment::class.java)) {
            val fragmentXpath = buildXpath(fullXpathFromClass(methodInfo.method.declaringClass))
            tmpFullXpath.add(FullXpath(fragmentXpath, clazz = methodInfo.method.declaringClass))
        }

        tmpFullXpath.add(FullXpath(xpath, clazz = methodInfo.method.declaringClass))

        if (methodInfo.method.isAnnotationPresent(khtml.annotations.Wait::class.java)) {
            waitConditionFragment(methodInfo.method, config.driver, tmpFullXpath)
        }
        when {
            isCustomElement(methodInfo.method.returnType) -> {
                return createCustomElement(
                        methodInfo.method.returnType,
                        buildXpath(tmpFullXpath),
                        config.driver,
                        config.testName
                )
            }
            isHtmlElement(methodInfo.method.returnType) -> {
                return createHtmlElement(
                        methodInfo.method.returnType as Class<khtml.element.HtmlElement>,
                        buildXpath(tmpFullXpath),
                        config.driver,
                        config.testName
                )
            }
            isCustomElementList(methodInfo.method) -> {
                val elements = safeOperation {
                    config.driver.findElements(By.xpath(buildXpath(tmpFullXpath)))
                } as List<WebElement>
                return (elements.indices).map {
                    createCustomElement(
                            methodInfo.method.returnMethodType!!,
                            buildXpathWithLastPosition(tmpFullXpath, it + 1),
                            config.driver,
                            config.testName
                    )
                }.toList()
            }
            isHtmlElementList(methodInfo.method) -> {
                val elements = safeOperation {
                    config.driver.findElements(By.xpath(buildXpath(tmpFullXpath)))
                } as List<WebElement>
                return (elements.indices).map {
                    createHtmlElement(
                            methodInfo.method.returnMethodType as Class<khtml.element.HtmlElement>,
                            buildXpathWithLastPosition(tmpFullXpath, it + 1),
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