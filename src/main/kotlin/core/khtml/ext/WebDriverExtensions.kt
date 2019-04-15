package core.khtml.ext

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriver
import java.io.File
import java.util.*


fun WebDriver.js(js: String): Any? {
    return (this as JavascriptExecutor).executeScript(js)
}

fun WebDriver.js(js: String, vararg args: Any): Any? {
    return (this as JavascriptExecutor).executeScript(js, *args)
}


fun WebDriver.saveScreenshot(dir: String, fileName: String = "") {
    if (fileName.isEmpty())
        (this as TakesScreenshot).getScreenshotAs(OutputType.FILE).copyTo(File("$dir/${UUID.randomUUID()}.png"))
    else
        (this as TakesScreenshot).getScreenshotAs(OutputType.FILE).copyTo(File("$dir/$fileName.png"))
}