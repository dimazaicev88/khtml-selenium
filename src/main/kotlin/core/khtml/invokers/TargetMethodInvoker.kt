package core.khtml.invokers

import core.khtml.conf.Configuration
import core.khtml.utils.ReflectUtils.findMethod

/**
 * Target method invoker.
 */
class TargetMethodInvoker : MethodInvoker {

    override fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): Any {
        val methodObj = when {
            !methodInfo.args.isNullOrEmpty() -> {
                val args: Array<Class<*>> = Array(methodInfo.args.size) { it::class.java }
                findMethod(config.target!!::class.java, methodInfo.method.name, *args)
            }
            else -> {
                findMethod(config.target!!::class.java, methodInfo.method.name)
            }
        }
        return if (!methodInfo.args.isNullOrEmpty()) {
            methodObj.invoke(config.target, *methodInfo.args)
        } else {
            methodObj.invoke(config.target)
        }
    }
}
