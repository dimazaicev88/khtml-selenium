package khtml.invokers

import khtml.conf.Configuration

interface MethodInvoker {
    fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): Any?
}