package khtml.invokers


import com.google.gson.Gson
import khtml.annotations.JSCall
import khtml.conf.Configuration
import khtml.ext.isListReturn
import khtml.ext.returnMethodType
import khtml.utils.ReflectUtils.getMethodParams
import org.openqa.selenium.JavascriptExecutor
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Paths

@Suppress("ThrowableNotThrown")
class JSCallInvoker : MethodInvoker {

    //TODO добавить определение типа у параметра
    override fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): Any? {
        val annotation = methodInfo.method.getAnnotation(JSCall::class.java)
        if (annotation.funcName.isEmpty() && annotation.js.isEmpty()) {
            RuntimeException("Empty funcName and js")
        }
        val resultJs: String?
        val executor = (config.driver as JavascriptExecutor)
        val callBackSelenium = "let callback = arguments[arguments.length - 1];"
        if (annotation.funcName.isNotEmpty()) {
            val listParams = getMethodParams(methodInfo.method, methodInfo.args).map { "'${it.value}'" }.toList().joinToString(separator = ",")
            val jsFile = File(Paths.get("src", "test", "resources", "js", methodInfo.method.declaringClass.simpleName + ".js").toAbsolutePath().toString())
            if (!jsFile.exists()) {
                FileNotFoundException()
            }
            val fileContent = jsFile.readText()
            val funcName = annotation.funcName

            resultJs = if (annotation.async) {
                executor.executeAsyncScript("""$fileContent $callBackSelenium  $funcName($listParams,callback) """)?.toString()
            } else {
                executor.executeScript("""$fileContent $funcName($listParams) """)?.toString()
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