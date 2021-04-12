package org.intsite.khtml.invokers

import org.intsite.khtml.annotations.Element
import org.intsite.khtml.annotations.NotUseParentXpath
import org.intsite.khtml.annotations.Wait
import org.intsite.khtml.build.XpathBuilder.Companion.buildXpath
import org.intsite.khtml.build.XpathBuilder.Companion.buildXpathWithLastPosition
import org.intsite.khtml.conf.Configuration
import org.intsite.khtml.conf.XpathItem
import org.intsite.khtml.element.HtmlElement
import org.intsite.khtml.ext.*
import org.intsite.khtml.ext.isReturnCustomElementList
import org.intsite.khtml.ext.isFragment
import org.intsite.khtml.ext.returnMethodType
import org.intsite.khtml.utils.MethodWrapper
import org.intsite.khtml.utils.ReflectUtils.createCustomElement
import org.intsite.khtml.utils.ReflectUtils.createHtmlElement
import org.intsite.khtml.utils.ReflectUtils.getMethodParams
import org.intsite.khtml.utils.ReflectUtils.replaceParams
import org.intsite.khtml.utils.WebDriverUtils.safeOperation
import org.intsite.khtml.utils.WebDriverUtils.waitConditionFragment
import org.openqa.selenium.By
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.WebElement
import java.util.*


class ElementInvoker : MethodInvoker {

    @Suppress("UNCHECKED_CAST")
    override fun invoke(proxy: Any, methodWrapper: MethodWrapper, config: Configuration): Any {
        val mapParams = getMethodParams(methodWrapper.method, methodWrapper.args)
        val template: String = methodWrapper.method.getAnnotation(Element::class.java).xpath
        val xpath = replaceParams(template, mapParams)

        if (config.xpathItems.size > 0 && config.xpathItems.last.clazz == methodWrapper.method.declaringClass) {
            config.xpathItems.removeLast()
        }

        val tmpFullXpath = config.xpathItems.clone() as LinkedList<XpathItem>

        if (methodWrapper.method.declaringClass.isAssignableFrom(config.parentClass)) {
            tmpFullXpath.clear()
        }

        if (methodWrapper.method.isAnnotationPresent(NotUseParentXpath::class.java)) {
            tmpFullXpath.clear()
        }

        if (tmpFullXpath.size > 0) {
            tmpFullXpath.last.position = config.instanceId
        }

        if (methodWrapper.method.declaringClass.isFragment && !methodWrapper.method.isAnnotationPresent(NotUseParentXpath::class.java)) {
            val fragmentXpath = buildXpath(methodWrapper.method.declaringClass.allXpathItemsByClass)
            tmpFullXpath.add(XpathItem(fragmentXpath, clazz = methodWrapper.method.declaringClass))
        }

        tmpFullXpath.add(XpathItem(xpath, clazz = methodWrapper.method.declaringClass))

        if (methodWrapper.method.isAnnotationPresent(Wait::class.java)) {
            waitConditionFragment(methodWrapper.method, config.driver, tmpFullXpath)
        }
        when {
            methodWrapper.method.returnType.isCustomElement -> {
                return createCustomElement(
                        methodWrapper.method.returnType,
                        buildXpath(tmpFullXpath),
                        config.driver
                )
            }
            methodWrapper.method.returnType.isHtmlElement -> {
                return createHtmlElement(
                        methodWrapper.method.returnType as Class<HtmlElement>,
                        buildXpath(tmpFullXpath),
                        config.driver
                )
            }
            methodWrapper.method.isReturnCustomElementList -> {
                val elements = safeOperation {
                    config.driver.findElements(By.xpath(buildXpath(tmpFullXpath)))
                } as List<WebElement>
                return (elements.indices).map {
                    createCustomElement(
                            methodWrapper.method.returnMethodType!!,
                            buildXpathWithLastPosition(tmpFullXpath, it + 1),
                            config.driver
                    )
                }.toList()
            }
            methodWrapper.method.isReturnHtmlElementList -> {
                val elements = safeOperation {
                    config.driver.findElements(By.xpath(buildXpath(tmpFullXpath)))
                } as List<WebElement>
                return (elements.indices).map {
                    createHtmlElement(
                            methodWrapper.method.returnMethodType as Class<HtmlElement>,
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