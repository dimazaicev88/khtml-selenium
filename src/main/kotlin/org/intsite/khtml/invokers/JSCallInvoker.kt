package org.intsite.khtml.invokers

import com.google.gson.Gson
import org.intsite.khtml.annotations.JSCall
import org.intsite.khtml.ext.isListReturn
import org.intsite.khtml.ext.returnMethodType
import org.intsite.khtml.conf.Configuration
import org.intsite.khtml.utils.MethodWrapper
import org.intsite.khtml.utils.ReflectUtils.getMethodParams
import org.openqa.selenium.JavascriptExecutor

//TODO переписать, так как сейчас файлы с js подключаются автоматом
@Suppress("ThrowableNotThrown")
class JSCallInvoker : MethodInvoker {

    //TODO добавить определение типа у параметра
    override fun invoke(proxy: Any, methodWrapper: MethodWrapper, config: Configuration): Any? {
        val annotation = methodWrapper.method.getAnnotation(JSCall::class.java)
        if (annotation.funcName.isEmpty() && annotation.js.isEmpty()) {
            RuntimeException("Empty funcName and js")
        }
        val resultJs: String?
        val executor = (config.driver as JavascriptExecutor)
        val callBackSelenium = "let callback = arguments[arguments.length - 1];"
        resultJs = if (annotation.funcName.isNotEmpty()) {
            val listParams = getMethodParams(methodWrapper.method, methodWrapper.args).map { "'${it.value}'" }.toList().joinToString(separator = ",")
            val funcName = annotation.funcName
            if (annotation.async) {
                executor.executeAsyncScript("""$callBackSelenium  $funcName($listParams,callback) """)?.toString()
            } else {
                executor.executeScript("""$funcName($listParams) """)?.toString()
            }
        } else {
            if (annotation.async) {
                executor.executeAsyncScript(annotation.js)?.toString()
            } else {
                executor.executeScript("$callBackSelenium  ${annotation.js}")?.toString()
            }
        }
        return if (methodWrapper.method.isListReturn) {
            Gson().fromJson(resultJs, methodWrapper.method.returnMethodType)
        } else {
            if (methodWrapper.method.returnMethodType.toString() == "void") {
                ""
            } else {
                Gson().fromJson(resultJs, methodWrapper.method.returnMethodType)
            }
        }
    }
}