package org.intsite.khtml.invokers

import org.intsite.khtml.conf.Configuration

interface MethodInvoker {
    fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): Any?
}