package org.intsite.khtml.utils

import com.google.common.collect.Lists
import org.apache.commons.lang3.ClassUtils
import org.apache.commons.lang3.reflect.ConstructorUtils.invokeConstructor
import org.intsite.khtml.annotations.*
import org.intsite.khtml.element.CustomElement
import org.intsite.khtml.element.HtmlElement
import org.intsite.khtml.ext.isListReturn
import org.intsite.khtml.ext.returnMethodType
import org.intsite.khtml.invokers.*
import org.openqa.selenium.WebDriver
import java.lang.reflect.*
import java.util.*

object ReflectUtils {

    @Suppress("UNCHECKED_CAST")
    fun <T> createProxy(type: Class<T>, handler: InvocationHandler): T = Proxy.newProxyInstance(
        type.classLoader,
        arrayOf(type),
        handler
    ) as T

    private fun <T> newInstance(clazz: Class<T>, vararg args: Any?): T {
        if (clazz.isMemberClass && !Modifier.isStatic(clazz.modifiers)) {
            val outerClass = clazz.declaringClass
            val outerObject = outerClass.newInstance()
            return invokeConstructor(clazz, *Lists.asList(outerObject, args).toTypedArray())
        }
        return invokeConstructor(clazz, *args)
    }

    fun <T> createCustomElement(
        clazz: Class<out T>, xpath: String, driver: WebDriver, testName: String? = null
    ): T = newInstance(clazz, xpath, driver, testName)

    fun createHtmlElement(
        clazz: Class<out HtmlElement>, xpath: String, driver: WebDriver, testName: String? = null
    ): HtmlElement = newInstance(clazz, xpath, driver, testName)

    fun findMethod(cls: Class<*>, methodName: String, vararg parameterTypes: Class<*>): Method {
        return listOf(cls).plus(ClassUtils.getAllSuperclasses(cls)).plus(
            ClassUtils.getAllInterfaces(cls).plus(cls)
        ).map { it.declaredMethods }.flatMap { it.toList() }.first {
            (it.name == methodName && Objects.deepEquals(parameterTypes, it.parameterTypes))
                    ||
                    (methodName == it.name && ClassUtils.isAssignable(parameterTypes, it.parameterTypes, true))
        }
    }

    fun getMapInvoker(
        clazz: Class<*>,
        method: Method,
        args: Array<out Any>?
    ): HashMap<Method, MethodInvoker> {
        val mapInvoker: HashMap<Method, MethodInvoker> = hashMapOf()
        val listMethods = arrayListOf<Method>()
        listMethods.addAll(clazz.declaredMethods.toMutableList())
        ClassUtils.getAllInterfaces(clazz).map { listMethods.addAll(it.declaredMethods.toMutableList()) }
        listMethods.forEach {
            when {
                it.isAnnotationPresent(Element::class.java) -> {
                    mapInvoker[it] = ElementInvoker()
                }
                it.isListReturn && (it.isAnnotationPresent(Fragment::class.java) || (it.returnMethodType != null && it.returnMethodType!!.isAnnotationPresent(
                    Fragment::class.java
                )))
                -> {
                    mapInvoker[it] = FragmentListInvoker()
                }
                !it.isListReturn && (it.isAnnotationPresent(Fragment::class.java) || (it.returnMethodType != null && it.returnMethodType!!.isAnnotationPresent(
                    Fragment::class.java
                )))
                -> {
                    mapInvoker[it] = FragmentInvoker()
                }
                it.isAnnotationPresent(InjectContext::class.java) -> {
                    mapInvoker[it] = ContextInjectInvoker()
                }
                it.isAnnotationPresent(InjectWebDriver::class.java) -> {
                    mapInvoker[it] = WebDriverInjectInvoker()
                }
                it.isAnnotationPresent(JSCall::class.java) -> {
                    mapInvoker[it] = JSCallInvoker()
                }
                isDefaultMethod(method, args) -> {
                    mapInvoker[it] = DefaultMethodInvoker()
                }
                else -> mapInvoker[it] = TargetMethodInvoker()
            }
        }
        return mapInvoker
    }

    fun replaceParams(template: String, mapParam: Map<String, String>): String =
        mapParam.entries.stream()
            .reduce(template, { a, b -> a.replace("#{" + b.key + "}", b.value) }, { s, _ -> s })

    private fun getParameterName(element: AnnotatedElement): String = element.getAnnotation(Param::class.java).value

    private fun hasParameterAnnotation(element: AnnotatedElement): Boolean =
        element.isAnnotationPresent(Param::class.java)

    fun getMethodParams(method: Method, args: Array<out Any>?): Map<String, String> {
        if (args.isNullOrEmpty()) return hashMapOf()
        return (0 until method.parameterCount)
            .filter { index -> hasParameterAnnotation(method.parameters[index]) }
            .map { index -> getParameterName(method.parameters[index]) to Objects.toString(args[index]) }.toMap()

    }

    private fun isDefaultMethod(method: Method, args: Array<out Any>?): Boolean {
        var argsForSearchMethod: Array<Class<*>> = Array(1) { method.declaringClass }
        if (!args.isNullOrEmpty()) {
            argsForSearchMethod += Array(args.size) { args[it]::class.java }
        }
        return listOf(method.declaringClass.declaredClasses.toList()).flatMap { it.toList() }
            .map { it.declaredMethods.toList() }
            .flatMap { it.toList() }
            .any { it.name == method.name && Objects.deepEquals(it.parameterTypes, argsForSearchMethod) }
    }

    fun findAnnotation(clazz: Class<*>, annotationClass: Class<out Annotation>): Boolean {
        if (clazz.isAnnotationPresent(annotationClass))
            return true
        val list = ClassUtils.getAllInterfaces(clazz)
        for (cls in list) {
            if (findAnnotation(cls, annotationClass))
                return true
        }
        return false
    }
}