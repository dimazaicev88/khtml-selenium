package core.khtml.invokers

import core.khtml.conf.Configuration

interface MethodInvoker {
    fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): Any?
}