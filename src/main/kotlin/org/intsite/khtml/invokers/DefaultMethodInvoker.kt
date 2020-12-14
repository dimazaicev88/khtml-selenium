package org.intsite.khtml.invokers

import org.intsite.khtml.conf.Configuration
import org.intsite.khtml.ext.findKotlinDefaultMethod
import org.intsite.khtml.utils.MethodWrapper

class DefaultMethodInvoker : MethodInvoker {


    override fun invoke(proxy: Any, methodWrapper: MethodWrapper, config: Configuration): Any? {
        val proxyTarget = config.proxyCache[methodWrapper.method.declaringClass]
        var argsForSearchMethod: Array<Class<*>> = Array(1) { methodWrapper.method.declaringClass }
        val methodObj = when {
            !methodWrapper.args.isNullOrEmpty() -> {
                argsForSearchMethod += Array(methodWrapper.args.size) {
                    methodWrapper.args[it]::class.java
                }
                methodWrapper.method.declaringClass.findKotlinDefaultMethod(
                        methodWrapper.method.name,
                        *argsForSearchMethod
                )
            }
            else -> {
                methodWrapper.method.declaringClass.findKotlinDefaultMethod(
                        methodWrapper.method.name,
                        *argsForSearchMethod
                )
            }
        }
        return if (!methodWrapper.args.isNullOrEmpty()) {
            var argsInvoke = arrayOf(proxy)
            methodWrapper.args.forEach {
                argsInvoke = argsInvoke.plus(it)
            }
            methodObj.invoke(null, *argsInvoke)
        } else {
            methodObj.invoke(null, proxyTarget)
        }
    }
}