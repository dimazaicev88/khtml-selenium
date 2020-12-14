package org.intsite.khtml.invokers

import org.intsite.khtml.conf.Configuration
import org.intsite.khtml.utils.MethodWrapper
import org.intsite.khtml.utils.ReflectUtils.findMethod
import java.util.*

/**
 * Target method invoker.
 */
class TargetMethodInvoker : MethodInvoker {

    override fun invoke(proxy: Any, methodWrapper: MethodWrapper, config: Configuration): Any {
        val methodObj = when {
            !methodWrapper.args.isNullOrEmpty() -> {
                val args: Array<Class<*>> = Array(methodWrapper.args.size) { it::class.java }
                if (config.target!!::class.java.superclass.isAssignableFrom(AbstractList::class.java)) {
                    findMethod(List::class.java, methodWrapper.method.name, *args)
                } else {
                    findMethod(config.target!!::class.java, methodWrapper.method.name, *args)
                }
            }
            else -> {
                if (config.target!!::class.java.superclass.isAssignableFrom(AbstractList::class.java)) {
                    findMethod(List::class.java, methodWrapper.method.name)
                } else {
                    findMethod(config.target!!::class.java, methodWrapper.method.name)
                }
            }
        }
        return if (!methodWrapper.args.isNullOrEmpty()) {
            methodObj.invoke(config.target, *methodWrapper.args)
        } else {
            methodObj.invoke(config.target)
        }
    }
}
