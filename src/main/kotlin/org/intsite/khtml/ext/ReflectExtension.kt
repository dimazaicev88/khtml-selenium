package org.intsite.khtml.ext

import org.apache.commons.lang3.ClassUtils
import org.intsite.khtml.annotations.Fragment
import org.intsite.khtml.annotations.Page
import org.intsite.khtml.conf.XpathItem
import org.intsite.khtml.element.CustomElement
import org.intsite.khtml.element.HtmlElement
import org.intsite.khtml.utils.ReflectUtils
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.util.*


internal val Class<*>.isHtmlElement
    get() = HtmlElement::class.java.isAssignableFrom(this)

internal val Class<*>.isCustomElement
    get() = CustomElement::class.java.isAssignableFrom(this)

internal val Method.isReturnCustomElementList: Boolean
    get() {
        val isList = List::class.java.isAssignableFrom(this.returnType)
        val typeArguments = this.returnMethodType
        return isList && typeArguments!!.isCustomElement
    }

internal val Method.isReturnHtmlElementList: Boolean
    get() {
        val isList = List::class.java.isAssignableFrom(this.returnType)
        val typeArguments = this.returnMethodType
        return isList && typeArguments!!.isHtmlElement
    }

internal val Method.xpathForFragment: LinkedList<XpathItem>
    get() {
        return when {
            this.isAnnotationPresent(Fragment::class.java) -> {
                val xpathItems = LinkedList<XpathItem>()
                xpathItems.add(
                        XpathItem(
                                this.getAnnotation(Fragment::class.java).xpath,
                                this.declaringClass
                        )
                )
                xpathItems
            }
            !this.isListReturn && this.returnType?.isFragment!! -> {
                this.returnType.allXpathItemsByClass
            }
            this.isListReturn && this.returnMethodType?.isFragment!! -> {
                this.returnMethodType!!.allXpathItemsByClass
            }
            else -> LinkedList<XpathItem>()
        }
    }


internal val Method.isListReturn: Boolean
    get() {
        return List::class.java.isAssignableFrom(returnType)
    }

internal val Method.returnMethodType: Class<*>?
    get() {
        return if (!isListReturn) {
            if (returnType is Class<*>) {
                returnType
            } else {
                this.declaringClass
            }
        } else {
            val type = (genericReturnType as ParameterizedType).actualTypeArguments[0]
            if (type is Class<*>) {
                type
            } else {
                this.declaringClass
            }
        }
    }

internal val Class<*>.isGenericInterface
    get() = this.genericInterfaces.isNotEmpty()

internal val Class<*>.mapGeneric: Map<Class<*>, Class<*>>
    get() {
        val map = hashMapOf<Class<*>, Class<*>>()
        this.genericInterfaces.map {
            if (it is ParameterizedType)
                map[it.rawType as Class<*>] = Class.forName(it.actualTypeArguments[0].typeName)
        }
        return map
    }

internal val Class<*>.isFragment: Boolean
    get() = ReflectUtils.findAnnotation(this, Fragment::class.java)


val Class<*>.isPage
    get() = this.isAnnotationPresent(Page::class.java)

val Method.xpathThisMethod: String
    get() {
        if (!this.isAnnotationPresent(Fragment::class.java)) {
            throw  RuntimeException("Annotation Fragment is not exists")
        }
        return this.getAnnotation(Fragment::class.java).xpath
    }

fun Class<*>.findKotlinDefaultMethod(methodName: String, vararg parameterTypes: Class<*>): Method {
    return listOf(this.declaredClasses.toList()).flatMap { it.toList() }.map { it.declaredMethods.toList() }
            .flatMap { it.toList() }.first {
                it.name == methodName && Objects.deepEquals(parameterTypes, it.parameterTypes)
            }
}

val Class<*>.allXpathItemsByClass: LinkedList<XpathItem>
    get() {
        val listXpath = LinkedList<XpathItem>()

        if (this.isAnnotationPresent(Fragment::class.java)) {
            listXpath.add(
                    XpathItem(
                            this.getAnnotation(Fragment::class.java).xpath.replaceFirst(".", ""),
                            this
                    )
            )
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
                    listXpath.add(
                            XpathItem(
                                    interfacesWithAnnotation[0].getAnnotation(Fragment::class.java).xpath,
                                    clazz
                            )
                    )
                }

                for (cls in list) {
                    findXpath(cls)
                }
            }
        }
        findXpath(this)
        listXpath.reverse()
        return listXpath
    }