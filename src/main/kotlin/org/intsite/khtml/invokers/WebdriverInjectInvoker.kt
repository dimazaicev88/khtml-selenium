package org.intsite.khtml.invokers

import org.intsite.khtml.conf.Configuration
import org.intsite.khtml.utils.MethodWrapper

class WebDriverInjectInvoker : MethodInvoker {

    override fun invoke(proxy: Any, methodWrapper: MethodWrapper, config: Configuration): Any {
        return config.driver
    }
}