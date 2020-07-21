package org.intsite.khtml.invokers

import org.intsite.khtml.conf.Configuration
import org.intsite.khtml.utils.ReflectUtils.findMethod
import java.util.*

/**
 * Target method invoker.
 */
class TargetMethodInvoker : MethodInvoker {

    override fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): Any {
        val methodObj = when {
            !methodInfo.args.isNullOrEmpty() -> {
                val args: Array<Class<*>> = Array(methodInfo.args.size) { it::class.java }
                if (config.target!!::class.java.superclass.isAssignableFrom(AbstractList::class.java)) {
                    findMethod(List::class.java, methodInfo.method.name, *args)
                } else {
                    findMethod(config.target!!::class.java, methodInfo.method.name, *args)
                }
            }
            else -> {
                if (config.target!!::class.java.superclass.isAssignableFrom(AbstractList::class.java)) {
                    findMethod(List::class.java, methodInfo.method.name)
                } else {
                    findMethod(config.target!!::class.java, methodInfo.method.name)
                }
            }
        }
        return if (!methodInfo.args.isNullOrEmpty()) {
            methodObj.invoke(config.target, *methodInfo.args)
        } else {
            methodObj.invoke(config.target)
        }
    }
}
