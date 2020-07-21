package org.intsite.khtml.invokers

import org.intsite.khtml.conf.Configuration
import org.intsite.khtml.utils.ReflectUtils.getMapInvoker
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

class ProxyHandler(private val configuration: Configuration, var instanceId: Int = 0) : InvocationHandler {

    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {
        if (Any::class.java == method.declaringClass) {
            return if (args != null) {
                method.invoke(this, *args)
            } else
                method.invoke(this)
        }
        val handler: MethodInvoker?
        val mapInvokers = getMapInvoker(method.declaringClass, method, args)
        handler = mapInvokers[method]
        configuration.instanceId = instanceId
        return handler?.invoke(proxy, MethodInfo(method, args), configuration)
    }
}
