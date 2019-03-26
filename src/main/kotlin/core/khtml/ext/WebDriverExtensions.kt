package core.khtml.ext

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver

fun WebDriver.js(js: String): Any? {
    return (this as JavascriptExecutor).executeScript(js)
}

fun WebDriver.js(js: String, vararg args: Any): Any? {
    return (this as JavascriptExecutor).executeScript(js, *args)
}