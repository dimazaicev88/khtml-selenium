package khtml.invokers

import khtml.conf.Configuration

class WebDriverInjectInvoker : MethodInvoker {

    override fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): Any {
        return config.driver
    }
}