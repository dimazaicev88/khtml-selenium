package core.khtml.ext

import core.khtml.utils.WebDriverUtils.safeOperation
import org.openqa.selenium.Cookie
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.NoAlertPresentException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.WebDriverWait

fun WebDriver.js(js: String): Any? {
    return safeOperation {
        (this as JavascriptExecutor).executeScript(js)
    }
}

fun WebDriver.js(js: String, vararg args: Any): Any? {
    return (this as JavascriptExecutor).executeScript(js, *args)
}

fun WebDriver.alertAccept() {
    try {
        switchTo().alert().accept()
    } catch (ignore: Exception) {

    }
}

fun WebDriver.jsFindElement(xpath: String) = "document.evaluate(\"$xpath\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null)"

fun WebDriver.alertDismiss() {
    switchTo().alert().dismiss()
}

fun WebDriver.waitForLoad() {
    WebDriverWait(this, 30).until { it.js("return document.readyState") == "complete" } as Boolean
}

fun WebDriver.updateCookies(cookies: Set<Cookie>) {
    cookies.forEach {
        this.manage().addCookie(it)
    }
    this.navigate().refresh()
}

fun WebDriver.updateCookies(startPage: String, cookies: Set<Cookie>, refresh: Boolean = true) {
    this.get(startPage)
    cookies.forEach {
        this.manage().addCookie(it)
    }
    if (refresh)
        this.navigate().refresh()
}

fun WebDriver.waitWindow(countWindows: Long, timeOutInSeconds: Long) {
    WebDriverWait(this, timeOutInSeconds, 100).until {
        it.windowHandles
        it.windowHandles.size.toLong() == countWindows
    }
}

fun WebDriver.waitAlert(timeOut: Int) {
    WebDriverWait(this, timeOut.toLong())
            .ignoring(NoAlertPresentException::class.java)
            .until {
                try {
                    this.switchTo().alert()
                    true
                } catch (e: NoAlertPresentException) {
                    false
                }
            }
}

fun WebDriver.isAlertPresent(): Boolean {
    return try {
        this.switchTo().alert()
        true
    } catch (Ex: NoAlertPresentException) {
        false
    }
}

fun WebDriver.refresh() {
    this.get(this.currentUrl)
}