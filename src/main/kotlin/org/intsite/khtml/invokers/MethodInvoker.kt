package org.intsite.khtml.invokers

import org.intsite.khtml.conf.Configuration
import org.intsite.khtml.utils.MethodWrapper

interface MethodInvoker {
    fun invoke(proxy: Any, methodWrapper: MethodWrapper, config: Configuration): Any?
}