package core.khtml.utils

import com.google.common.collect.Lists
import core.khtml.annotations.*
import core.khtml.conf.FullXpath
import core.khtml.dump.DumpInfo
import core.khtml.element.CustomElement
import core.khtml.element.HtmlElement
import core.khtml.ext.isListReturn
import core.khtml.ext.returnMethodType
import core.khtml.invokers.*
import org.apache.commons.lang3.ClassUtils
import org.apache.commons.lang3.reflect.ConstructorUtils.invokeConstructor
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

    fun isHtmlElement(clazz: Class<*>): Boolean = HtmlElement::class.java.isAssignableFrom(clazz)

    private fun <T> newInstance(clazz: Class<T>, vararg args: Any): T {
        if (clazz.isMemberClass && !Modifier.isStatic(clazz.modifiers)) {
            val outerClass = clazz.declaringClass
            val outerObject = outerClass.newInstance()
            return invokeConstructor<T>(clazz, *Lists.asList(outerObject, args).toTypedArray())
        }
        return invokeConstructor<T>(clazz, *args)
    }

    fun isCustomElement(clazz: Class<*>): Boolean = CustomElement::class.java.isAssignableFrom(clazz)

    fun <T> createCustomElement(
        clazz: Class<out T>, xpath: String, driver: WebDriver, dumpInfo: DumpInfo? = null
    ): T = if (dumpInfo != null) {
        newInstance(clazz, xpath, driver, dumpInfo)
    } else {
        newInstance(clazz, xpath, driver)
    }


    fun createHtmlElement(
        clazz: Class<out HtmlElement>, xpath: String, driver: WebDriver, dumpInfo: DumpInfo? = null
    ): HtmlElement = if (dumpInfo != null) {
        newInstance(clazz, xpath, driver, dumpInfo)
    } else {
        newInstance(clazz, xpath, driver)
    }

    fun findMethod(cls: Class<*>, methodName: String, vararg parameterTypes: Class<*>): Method {
        return listOf(cls).plus(ClassUtils.getAllSuperclasses(cls)).plus(
            ClassUtils.getAllInterfaces(cls).plus(cls)
        ).map { it.declaredMethods }.flatMap { it.toList() }.first {
            (it.name == methodName && Objects.deepEquals(parameterTypes, it.parameterTypes))
                    ||
                    (methodName == it.name && ClassUtils.isAssignable(parameterTypes, it.parameterTypes, true))
        }
    }

    fun findKotlinDefaultMethod(cls: Class<*>, methodName: String, vararg parameterTypes: Class<*>): Method {
        return listOf(cls.declaredClasses.toList()).flatMap { it.toList() }.map { it.declaredMethods.toList() }
            .flatMap { it.toList() }.first {
                it.name == methodName && Objects.deepEquals(parameterTypes, it.parameterTypes)
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
                    mapInvoker[it] = FragmentInjectInvoker()
                }
                it.isAnnotationPresent(InjectWebDriver::class.java) -> {
                    mapInvoker[it] = WebDriverInjectInvoker()
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

    fun isCustomElementList(method: Method): Boolean {
        val isList = List::class.java.isAssignableFrom(method.returnType)
        val typeArguments = (method.genericReturnType as ParameterizedType).actualTypeArguments[0] as Class<*>
        return isList && isCustomElement(typeArguments)
    }

    fun isHtmlElementList(method: Method): Boolean {
        val isList = List::class.java.isAssignableFrom(method.returnType)
        val typeArguments = (method.genericReturnType as ParameterizedType).actualTypeArguments[0] as Class<*>
        return isList && isHtmlElement(typeArguments)
    }

    fun findFragmentTemplate(method: Method): String {
        return when {
            method.isAnnotationPresent(Fragment::class.java) -> {
                method.getAnnotation(Fragment::class.java).xpath
            }
            else -> ""
        }
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

    fun isFindAnnotation(clazz: Class<*>, annotationClass: Class<out Annotation>): Boolean {
        if (clazz.isAnnotationPresent(annotationClass))
            return true
        val listInterface = ClassUtils.getAllInterfaces(clazz)
        for (cls in listInterface) {
            if (isFindAnnotation(cls, annotationClass))
                return true
        }
        return false
    }

    fun fullXpathFromClass(clazz: Class<*>): LinkedList<FullXpath> {
        val listXpath = LinkedList<FullXpath>()

        if (clazz.isAnnotationPresent(Fragment::class.java)) {
            listXpath.add(FullXpath(clazz.getAnnotation(Fragment::class.java).xpath.replaceFirst(".", "")))
        }

        fun findXpath(clazz: Class<*>) {
            val list = ClassUtils.getAllInterfaces(clazz)
            if (list.size != 0) {
                val interfacesWithAnnotation = list.filter {
                    it.isAnnotationPresent(Fragment::class.java) && it.getAnnotation(Fragment::class.java).inheritance
                }

                if (interfacesWithAnnotation.size > 1) {
                    throw RuntimeException("More than one interface have a  annotation Fragment with the possibility of inheritance")
                }

                if (interfacesWithAnnotation.size == 1) {
                    listXpath.add(FullXpath(interfacesWithAnnotation[0].getAnnotation(Fragment::class.java).xpath))
                }

                for (cls in list) {
                    findXpath(cls)
                }
            }
        }
        findXpath(clazz)
        listXpath.reverse()
        return listXpath
    }

    fun getDumpInfo(method: Method): DumpInfo? {
        if (method.declaringClass.isAnnotationPresent(Dump::class.java)) {
            val dumpAnnotation = method.declaringClass.getAnnotation(Dump::class.java)
            return DumpInfo(
                dir = if (dumpAnnotation.dirDump.isNotEmpty()) {
                    dumpAnnotation.dirDump
                } else {
                    System.getProperty("khtml.dump.dir")
                },
                screenshot = dumpAnnotation.screenshot,
                condition = dumpAnnotation.condition,
                method = method
            )
        }
        return null
    }
}