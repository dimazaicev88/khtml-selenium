package org.intsite.khtml.invokers

import org.intsite.khtml.annotations.Fragment
import org.intsite.khtml.annotations.NotUseParentXpath
import org.intsite.khtml.annotations.Wait
import org.intsite.khtml.conf.Configuration
import org.intsite.khtml.conf.XpathItem
import org.intsite.khtml.ext.*
import org.intsite.khtml.utils.MethodWrapper
import org.intsite.khtml.utils.ReflectUtils.createProxy
import org.intsite.khtml.utils.ReflectUtils.getMethodParams
import org.intsite.khtml.utils.ReflectUtils.replaceParams
import org.intsite.khtml.utils.WebDriverUtils.waitConditionFragment


class FragmentInvoker : MethodInvoker {

    @Suppress("UNCHECKED_CAST")
    override fun invoke(proxy: Any, methodWrapper: MethodWrapper, config: Configuration): Any {
        val mapParams = getMethodParams(methodWrapper.method, methodWrapper.args)
        val mapGenerics = config.parentClass.mapGeneric
        val parentClassForMethod = methodWrapper.method.declaringClass

        if (methodWrapper.method.declaringClass.isAssignableFrom(config.parentClass)) {
            config.xpathItems.clear()
        }
        if (methodWrapper.method.isAnnotationPresent(NotUseParentXpath::class.java)) {
            config.xpathItems.clear()
        }

        /**
         * Если интерфейс Generic
         */
        if (config.parentClass.isGenericInterface) {
            val classForMethod = methodWrapper.method.declaringClass

            /**
             * Если метод возвращает не класс Generic
             */
            if (!mapGenerics.containsKey(classForMethod)) {
                if (!classForMethod.isFragment) {
                    throw RuntimeException("${classForMethod.simpleName} is not a Fragment")
                }

                /**
                 * Добавляем Xpath для класса и всех его родителей
                 */
                if (!methodWrapper.method.isAnnotationPresent(NotUseParentXpath::class.java))
                    config.xpathItems.addAll(parentClassForMethod.allXpathItemsByClass)

                /**
                 * Добавляем Xpath для метода и возвращаемого класса
                 */
                config.xpathItems.addAll(
                        methodWrapper.method.xpathForFragment.map {
                            XpathItem(replaceParams(it.xpath, mapParams), it.clazz, it.position)
                        }
                )
                return config.proxyCache.computeIfAbsent(methodWrapper.method.returnMethodType!!) {
                    createProxy(methodWrapper.method.returnMethodType!!, ProxyHandler(config))
                }
            } else {
                val classForProxy = config.parentClass.mapGeneric[methodWrapper.method.declaringClass]

                if (!methodWrapper.method.isAnnotationPresent(NotUseParentXpath::class.java))
                    config.xpathItems.addAll(parentClassForMethod.allXpathItemsByClass)

                config.xpathItems.addAll(
                        methodWrapper.method.xpathForFragment.map {
                            XpathItem(replaceParams(it.xpath, mapParams), methodWrapper.method.declaringClass, it.position)
                        }
                )
                return config.proxyCache.computeIfAbsent(classForProxy!!) {
                    createProxy(classForProxy, ProxyHandler(config))
                }
            }
        } else {

            if (config.xpathItems.size > 0 && config.xpathItems.last.clazz == methodWrapper.method.declaringClass) {
                config.xpathItems.removeLast()
            }

            if (config.xpathItems.size > 0) {
                config.xpathItems.last.position = config.instanceId
            }

            config.target = methodWrapper.method.declaringClass

            if (methodWrapper.method.declaringClass.isFragment && !methodWrapper.method.isAnnotationPresent(NotUseParentXpath::class.java))
                config.xpathItems.addAll(methodWrapper.method.declaringClass.allXpathItemsByClass)

            val fullXpath = methodWrapper.method.xpathForFragment.map {
                XpathItem(replaceParams(it.xpath, mapParams), it.clazz, it.position)
            }
            config.xpathItems.addAll(fullXpath)

            if (methodWrapper.method.isAnnotationPresent(Wait::class.java)) {
                waitConditionFragment(methodWrapper.method, config.driver, config.xpathItems)
            }

            return config.proxyCache.computeIfAbsent(methodWrapper.method.returnType) {
                createProxy(methodWrapper.method.returnType, ProxyHandler(config))
            }
        }
    }
}