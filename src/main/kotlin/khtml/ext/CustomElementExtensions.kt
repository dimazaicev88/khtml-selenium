package khtml.ext

import ext.localStorage
import khtml.element.CustomElement
import khtml.waits.WaitElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.FluentWait
import tools.benchmark.benchMark

const val seleniumAjax = "selenium_ajax"
const val ajaxContentReady = "ajaxContentReady"
const val fancyBoxComplete = "fancyBoxComplete"
const val fancyBoxClosed = "fancyBoxClosed"
const val fancyBoxCleanup = "fancyBoxCleanup"
private val mapDefValueLS = mapOf(
        seleniumAjax to """{\"complete\":false, \"success\":false}""",
        ajaxContentReady to """false""",
        fancyBoxComplete to """false""",
        fancyBoxClosed to """false""",
        fancyBoxCleanup to """false"""
)


/**
 * Обновить Local Storage
 */
fun <T : CustomElement<T>> T.resetLS(): T {
    synchronized(driver) {
        mapDefValueLS.forEach { (key, value) ->
            driver.localStorage.removeItem(key)
            driver.localStorage[key] = value
        }
        return this
    }
}

fun <T : CustomElement<T>> T.waitCompleteSuccessAjaxAfter(timeOut: Long = 15, func: (T) -> Unit): T {
    synchronized(driver) {
        this.resetLS()
        func(this)
        wait.waitCustomCondition(timeOut = timeOut) {
            driver.js("""
                         var ajaxState= JSON.parse(localStorage.getItem('$seleniumAjax'));
                         return ajaxState.complete===true && ajaxState.success===true""").toString().toBoolean()
        }
        return this
    }
}

fun <T : CustomElement<T>> T.waitCompleteAjaxAfter(timeOut: Long = 15, func: (T) -> Unit): T {
    synchronized(driver) {
        this.resetLS()
        func(this)
        wait.waitCustomCondition(timeOut = timeOut) {
            driver.js("""
                         var ajaxState= JSON.parse(localStorage.getItem('$seleniumAjax'));
                         return ajaxState.complete===true""").toString().toBoolean()
        }
        return this
    }
}

fun <T : CustomElement<T>> T.waitAjax(timeOut: Long = 15): T {
    wait.waitCustomCondition(timeOut = timeOut) {
        driver.js("""var ajaxState= JSON.parse(localStorage.getItem('$seleniumAjax'));
                         return ajaxState.complete===true && ajaxState.success===true""").toString().toBoolean()
    }
    return this
}

fun <T : CustomElement<T>> T.clickAndWaitAjax(timeOut: Long = 15): T {
    this.resetLS()
    this.jse.click()
    wait.waitCustomCondition(timeOut = timeOut) {
        driver.js("""var ajaxState= JSON.parse(localStorage.getItem('$seleniumAjax'));
                         return ajaxState.complete===true && ajaxState.success===true""").toString().toBoolean()
    }
    return this
}

fun <T : CustomElement<T>> T.clickAndWaitFancyBoxComplete(timeOut: Long = 15): T {
    this.resetLS()
    this.jse.click()
    wait.waitCustomCondition(timeOut = timeOut) {
        driver.localStorage[fancyBoxComplete].toBoolean()
    }
    return this
}

fun <T : CustomElement<T>> T.clickAndWaitFancyBoxClosed(timeOut: Long = 15): T {
    this.resetLS()
    this.jse.click()
    wait.waitCustomCondition(timeOut = timeOut) {
        driver.localStorage[fancyBoxClosed].toBoolean()
    }
    return this
}

fun <T : CustomElement<T>> T.waitFancyBoxCompleteAfter(timeOut: Long = 15, func: (T) -> Unit): T {
    this.resetLS()
    func(this)
    wait.waitCustomCondition(timeOut = timeOut) {
        driver.localStorage[fancyBoxComplete].toBoolean()
    }
    return this
}

fun <T : CustomElement<T>> T.waitFancyBoxComplete(timeOut: Long = 15): T {
    wait.waitCustomCondition(timeOut = timeOut) {
        driver.localStorage[fancyBoxComplete].toBoolean()
    }
    return this
}

fun <T : CustomElement<T>> T.waitFancyBoxCleanup(timeOut: Long = 15): T {
    wait.waitCustomCondition(timeOut = timeOut) {
        driver.localStorage[fancyBoxCleanup].toBoolean()
    }
    return this
}

fun <T : CustomElement<T>> T.waitFancyBoxClosedAfter(timeOut: Long = 15, func: (T) -> Unit): T {
    this.resetLS()
    func(this)
    wait.waitCustomCondition(timeOut = timeOut) {
        driver.localStorage[fancyBoxClosed].toBoolean()
    }
    return this
}

fun <T : CustomElement<T>> T.waitFancyBoxClosed(timeOut: Long = 15): T {
    wait.waitCustomCondition(timeOut = timeOut) {
        driver.localStorage[fancyBoxClosed].toBoolean()
//        driver.js("""return localStorage.getItem('$fancyBoxClosed')""").toString().toBoolean()
    }
    return this
}

fun <T : CustomElement<T>> T.waitAjaxContentReadyAfter(timeOut: Long = 15, func: (T) -> Unit): T {
    synchronized(driver) {
        this.resetLS()
        func(this)
        wait.waitCustomCondition(timeOut = timeOut) {
            driver.localStorage[ajaxContentReady].toBoolean()
        }
        return this
    }
}

fun <T : CustomElement<T>> T.waitJqueryXHR(timeOut: Long = 30, polling: Long = 50, fw: FluentWait<WebDriver>? = null): T {
    benchMark("waitAjax") {
        WaitElement(this.driver, this.xpath).waitJqueryXHR(timeOut, polling, fw)
    }
    return this
}

fun <T : CustomElement<T>> T.waitTextPresent(
        timeOut: Long = 5,
        polling: Long = 50,
        fw: FluentWait<WebDriver>? = null
): T {
    WaitElement(this.driver, this.xpath).waitTextPresent(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitDisplay(timeOut: Long = 5, polling: Long = 50, fw: FluentWait<WebDriver>? = null): T {
    WaitElement(this.driver, this.xpath).waitDisplay(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitClickable(
        timeOut: Long = 5,
        polling: Long = 50,
        fw: FluentWait<WebDriver>? = null
): T {
    WaitElement(this.driver, this.xpath).waitClickable(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitInvisible(
        timeOut: Long = 5,
        polling: Long = 50,
        fw: FluentWait<WebDriver>? = null
): T {
    WaitElement(this.driver, this.xpath).waitInvisible(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitNotExists(
        timeOut: Long = 30,
        polling: Long = 50,
        fw: FluentWait<WebDriver>? = null
): T {
    WaitElement(this.driver, this.xpath).waitNotExists(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitEnabled(timeOut: Long = 5, polling: Long = 50, fw: FluentWait<WebDriver>? = null): T {
    WaitElement(this.driver, this.xpath).waitEnabled(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitDisabled(
        timeOut: Long = 5,
        polling: Long = 50,
        fw: FluentWait<WebDriver>? = null
): T {
    WaitElement(this.driver, this.xpath).waitDisabled(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitCustomCondition(
        timeOut: Long = 5,
        polling: Long = 50,
        condition: (element: T) -> Boolean
): T {
    WaitElement(this.driver, this.xpath).waitCustomCondition(timeOut, polling) { condition(this) }
    return this
}

fun <T : CustomElement<T>> T.sleep(time: Long = 150): T {
    Thread.sleep(time)
    return this
}