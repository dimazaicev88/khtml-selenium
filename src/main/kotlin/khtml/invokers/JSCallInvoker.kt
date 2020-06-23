package khtml.invokers

import com.google.gson.Gson
import khtml.ext.isListReturn
import khtml.ext.returnMethodType
import khtml.utils.ReflectUtils.getMethodParams
import org.openqa.selenium.JavascriptExecutor

//TODO переписать, так как сейчас файлы с js подключаются автоматом
@Suppress("ThrowableNotThrown")
class JSCallInvoker : MethodInvoker {

    //TODO добавить определение типа у параметра
    override fun invoke(proxy: Any, methodInfo: MethodInfo, config: khtml.conf.Configuration): Any? {
        val annotation = methodInfo.method.getAnnotation(khtml.annotations.JSCall::class.java)
        if (annotation.funcName.isEmpty() && annotation.js.isEmpty()) {
            RuntimeException("Empty funcName and js")
        }
        val resultJs: String?
        val executor = (config.driver as JavascriptExecutor)
        val callBackSelenium = "let callback = arguments[arguments.length - 1];"
        if (annotation.funcName.isNotEmpty()) {
            val listParams = getMethodParams(methodInfo.method, methodInfo.args).map { "'${it.value}'" }.toList().joinToString(separator = ",")
            val funcName = annotation.funcName
            resultJs = if (annotation.async) {
                executor.executeAsyncScript("""$callBackSelenium  $funcName($listParams,callback) """)?.toString()
            } else {
                executor.executeScript("""$funcName($listParams) """)?.toString()
            }
        } else {
            resultJs = if (annotation.async) {
                executor.executeAsyncScript(annotation.js)?.toString()
            } else {
                executor.executeScript("$callBackSelenium  ${annotation.js}")?.toString()
            }
        }
        return if (methodInfo.method.isListReturn) {
            Gson().fromJson(resultJs, methodInfo.method.returnMethodType)
        } else {
            if (methodInfo.method.returnMethodType.toString() == "void") {
                ""
            } else {
                Gson().fromJson(resultJs, methodInfo.method.returnMethodType)
            }
        }
    }
}