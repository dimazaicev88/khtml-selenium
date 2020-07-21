package org.intsite.khtml.invokers

import org.intsite.khtml.conf.Configuration
import org.intsite.khtml.utils.ReflectUtils.findKotlinDefaultMethod

class DefaultMethodInvoker : MethodInvoker {


    override fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): Any? {
        val proxyTarget = config.proxyCache[methodInfo.method.declaringClass]
        var argsForSearchMethod: Array<Class<*>> = Array(1) { methodInfo.method.declaringClass }
        val methodObj = when {
            !methodInfo.args.isNullOrEmpty() -> {
                argsForSearchMethod += Array(methodInfo.args.size) {
                    methodInfo.args[it]::class.java
                }
                findKotlinDefaultMethod(
                        methodInfo.method.declaringClass,
                        methodInfo.method.name,
                        *argsForSearchMethod
                )
            }
            else -> {
                findKotlinDefaultMethod(
                        methodInfo.method.declaringClass,
                        methodInfo.method.name,
                        *argsForSearchMethod
                )
            }
        }
        return if (!methodInfo.args.isNullOrEmpty()) {
            var argsInvoke = arrayOf(proxy)
            methodInfo.args.forEach {
                argsInvoke = argsInvoke.plus(it)
            }
            methodObj.invoke(null, *argsInvoke)
        } else {
            methodObj.invoke(null, proxyTarget)
        }
    }
}