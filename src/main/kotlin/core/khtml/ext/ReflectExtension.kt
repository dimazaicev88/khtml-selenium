package core.khtml.ext

import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType


internal val Method.isListReturn: Boolean
    get() {
        return List::class.java.isAssignableFrom(returnType)
    }

internal val Method.returnMethodType: Class<*>?
    get() {
        if (!isListReturn)
            return returnType
        val type = (genericReturnType as ParameterizedType).actualTypeArguments[0]
        if (type is Class<*>) {
            return type
        }
        return null
    }