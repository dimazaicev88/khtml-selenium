package org.intsite.khtml.invokers

import org.intsite.khtml.annotations.Fragment
import org.intsite.khtml.annotations.NotUseParentXpath
import org.intsite.khtml.annotations.Page
import org.intsite.khtml.annotations.Wait
import org.intsite.khtml.build.XpathBuilder.Companion.buildXpath
import org.intsite.khtml.conf.Configuration
import org.intsite.khtml.conf.XpathItem
import org.intsite.khtml.ext.*
import org.intsite.khtml.utils.MethodWrapper
import org.intsite.khtml.utils.ReflectUtils.createProxy
import org.intsite.khtml.utils.ReflectUtils.getMethodParams
import org.intsite.khtml.utils.ReflectUtils.replaceParams
import org.intsite.khtml.utils.WebDriverUtils.safeOperation
import org.intsite.khtml.utils.WebDriverUtils.waitConditionFragment
import org.openqa.selenium.By
import org.openqa.selenium.WebElement

class FragmentListInvoker : MethodInvoker {


    @Suppress("UNCHECKED_CAST")
    override fun invoke(proxy: Any, methodWrapper: MethodWrapper, config: Configuration): Any? {
        val mapParams = getMethodParams(methodWrapper.method, methodWrapper.args)
        val mapGenerics = config.parentClass.mapGeneric
        val parentClassForMethod = methodWrapper.method.declaringClass
        val proxyClass: Class<*>

        if (
                methodWrapper.method.declaringClass.isAnnotationPresent(Page::class.java) ||
                methodWrapper.method.declaringClass.isAssignableFrom(config.parentClass)
        ) {
            config.xpathItems.clear()
        }

        if (methodWrapper.method.isAnnotationPresent(NotUseParentXpath::class.java)) {
            config.xpathItems.clear()
        }

        if (config.xpathItems.size > 0 && config.xpathItems.last.clazz == methodWrapper.method.declaringClass) {
            config.xpathItems.removeLast()
        }

        if (config.xpathItems.size > 0) {
            config.xpathItems.last.position = config.instanceId
        }

        if (config.parentClass.isGenericInterface) {
            val classForMethod = methodWrapper.method.declaringClass
            val methodReturnedClass = methodWrapper.method.returnMethodType!!

            /**
             * Если интерфейс не Generic
             */
            if (!mapGenerics.containsKey(classForMethod)) {
                proxyClass = methodReturnedClass

                if (!methodWrapper.method.isAnnotationPresent(NotUseParentXpath::class.java)) {
                    config.xpathItems.addAll(parentClassForMethod.allXpathItemsByClass)
                }
                /**
                 * Добавляем Xpath для метода и возвращаемого класса
                 */
                config.xpathItems.addAll(
                        methodWrapper.method.xpathForFragment.map {
                            XpathItem(replaceParams(it.xpath, mapParams), it.clazz, it.position)
                        }
                )
            } else {
                proxyClass = mapGenerics.getValue(methodReturnedClass)
                config.xpathItems.addAll(methodReturnedClass.allXpathItemsByClass)
                config.xpathItems.add(
                        XpathItem(
                                replaceParams(methodWrapper.method.xpathThisMethod, mapParams),
                                mapGenerics.getValue(methodReturnedClass)
                        )
                )
            }
            config.instanceId = 0

            if (methodWrapper.method.isAnnotationPresent(Wait::class.java)) {
                waitConditionFragment(methodWrapper.method, config.driver, config.xpathItems)
            }
            val elements = safeOperation {
                config.driver.findElements(By.xpath(buildXpath(config.xpathItems)))
            } as List<WebElement>
            val listElements = (elements.indices).map {
                createProxy(proxyClass, ProxyHandler(config, instanceId = it))
            }.toList()
            config.target = listElements
            config.proxyCache.computeIfAbsent(methodWrapper.method.returnType) {
                createProxy(methodWrapper.method.returnType, ProxyHandler(config))
            }
            return config.proxyCache[methodWrapper.method.returnType]
        } else {
            if (methodWrapper.method.declaringClass.isFragment) {
                config.xpathItems.addAll(methodWrapper.method.declaringClass.allXpathItemsByClass)
            }

            val fullXpath = methodWrapper.method.xpathForFragment.map {
                XpathItem(
                        replaceParams(it.xpath, mapParams),
                        it.clazz,
                        it.position
                )
            }
            config.xpathItems.addAll(fullXpath)
            config.instanceId = 0

            if (methodWrapper.method.isAnnotationPresent(Wait::class.java)) {
                waitConditionFragment(methodWrapper.method, config.driver, config.xpathItems)
            }
            val elements = safeOperation {
                config.driver.findElements(By.xpath(buildXpath(config.xpathItems)))
            } as List<WebElement>
            val listElements = (elements.indices).map {
                val typeGeneric = methodWrapper.method.returnMethodType
                        ?: throw RuntimeException("Undefined generic type")
                createProxy(typeGeneric, ProxyHandler(config, instanceId = it))
            }.toList()
            config.target = listElements
            config.proxyCache.computeIfAbsent(methodWrapper.method.returnType) {
                createProxy(methodWrapper.method.returnType, ProxyHandler(config))
            }
            return config.proxyCache[methodWrapper.method.returnType]
        }
    }
}