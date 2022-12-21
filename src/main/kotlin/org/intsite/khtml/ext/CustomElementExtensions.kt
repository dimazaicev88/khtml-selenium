package org.intsite.khtml.ext

import org.intsite.khtml.element.CustomElement
import org.intsite.khtml.waits.WaitElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.FluentWait
import java.time.Duration

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

fun <T : CustomElement<T>> T.waitCompleteSuccessAjaxAfter(
    timeOut: Duration = Duration.ofSeconds(15),
    func: (T) -> Unit
): T {
    synchronized(driver) {
        this.resetLS()
        func(this)
        wait.waitCustomCondition(timeOut = timeOut) {
            driver.js(
                """
                    var ajaxState= JSON.parse(localStorage.getItem('$seleniumAjax'));
                   return ajaxState.complete===true && ajaxState.success===true"""
            ).toString().toBoolean()
        }
        return this
    }
}

fun <T : CustomElement<T>> T.waitCompleteAjaxAfter(timeOut: Duration = Duration.ofSeconds(15), func: (T) -> Unit): T {
    synchronized(driver) {
        this.resetLS()
        func(this)
        wait.waitCustomCondition(timeOut = timeOut) {
            driver.js(
                """
                    var ajaxState= JSON.parse(localStorage.getItem('$seleniumAjax'));
                    return ajaxState.complete===true"""
            ).toString().toBoolean()
        }
        return this
    }
}

fun <T : CustomElement<T>> T.waitAjax(
    polling: Duration = Duration.ofMillis(500),
    timeOut: Duration = Duration.ofSeconds(15)
): T {
    wait.waitCustomCondition(timeOut = timeOut, polling) {
        driver.js(
            """ var ajaxState= JSON.parse(localStorage.getItem('$seleniumAjax'));
                return ajaxState.complete===true && ajaxState.success===true"""
        ).toString().toBoolean()
    }
    return this
}

fun <T : CustomElement<T>> T.clickAndWaitAjax(timeOut: Duration = Duration.ofSeconds(15)): T {
    this.resetLS()
    this.jse.click()
    wait.waitCustomCondition(timeOut = timeOut) {
        driver.js(
            """ var ajaxState= JSON.parse(localStorage.getItem('$seleniumAjax'));
                return ajaxState.complete===true && ajaxState.success===true"""
        ).toString().toBoolean()
    }
    return this
}

fun <T : CustomElement<T>> T.clickAndWaitFancyBoxComplete(timeOut: Duration = Duration.ofSeconds(15)): T {
    this.resetLS()
    this.jse.click()
    wait.waitCustomCondition(timeOut = timeOut) {
        driver.localStorage[fancyBoxComplete].toBoolean()
    }
    return this
}

fun <T : CustomElement<T>> T.clickAndWaitFancyBoxClosed(timeOut: Duration = Duration.ofSeconds(15)): T {
    this.resetLS()
    this.jse.click()
    wait.waitCustomCondition(timeOut = timeOut) {
        driver.localStorage[fancyBoxClosed].toBoolean()
    }
    return this
}

fun <T : CustomElement<T>> T.waitFancyBoxCompleteAfter(
    timeOut: Duration = Duration.ofSeconds(15),
    func: (T) -> Unit
): T {
    this.resetLS()
    func(this)
    wait.waitCustomCondition(timeOut = timeOut) {
        driver.localStorage[fancyBoxComplete].toBoolean()
    }
    return this
}

fun <T : CustomElement<T>> T.waitFancyBoxComplete(timeOut: Duration = Duration.ofSeconds(15)): T {
    wait.waitCustomCondition(timeOut = timeOut) {
        driver.localStorage[fancyBoxComplete].toBoolean()
    }
    return this
}

fun <T : CustomElement<T>> T.waitFancyBoxCleanup(timeOut: Duration = Duration.ofSeconds(15)): T {
    wait.waitCustomCondition(timeOut = timeOut) {
        driver.localStorage[fancyBoxCleanup].toBoolean()
    }
    return this
}

fun <T : CustomElement<T>> T.waitFancyBoxClosedAfter(timeOut: Duration = Duration.ofSeconds(15), func: (T) -> Unit): T {
    this.resetLS()
    func(this)
    wait.waitCustomCondition(timeOut = timeOut) {
        driver.localStorage[fancyBoxClosed].toBoolean()
    }
    return this
}

fun <T : CustomElement<T>> T.waitFancyBoxClosed(timeOut: Duration = Duration.ofSeconds(15)): T {
    wait.waitCustomCondition(timeOut = timeOut) {
        driver.localStorage[fancyBoxClosed].toBoolean()
        driver.js("""return localStorage.getItem('$fancyBoxClosed')""").toString().toBoolean()
    }
    return this
}

fun <T : CustomElement<T>> T.waitAjaxContentReadyAfter(
    timeOut: Duration = Duration.ofSeconds(15),
    func: (T) -> Unit
): T {
    synchronized(driver) {
        this.resetLS()
        func(this)
        wait.waitCustomCondition(timeOut = timeOut) {
            driver.localStorage[ajaxContentReady].toBoolean()
        }
        return this
    }
}

fun <T : CustomElement<T>> T.clickAndWaitAjaxComplete(
    polling: Duration = Duration.ofMillis(500),
    timeOut: Duration = Duration.ofSeconds(15)
): T {
    this.resetLS()
    this.jse.click()
    wait.waitCustomCondition(polling = polling, timeOut = timeOut) {
        driver.js(
            """var ajaxState= JSON.parse(localStorage.getItem('$seleniumAjax'));
                         return ajaxState.complete===true"""
        ).toString().toBoolean()
    }
    return this
}


fun <T : CustomElement<T>> T.waitJqueryXHR(
    timeOut: Duration = Duration.ofSeconds(30),
    polling: Duration = Duration.ofMillis(500),
    fw: FluentWait<WebDriver>? = null
): T {
    WaitElement(this.driver, this.xpath).waitJqueryXHR(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitTextPresent(
    timeOut: Duration = Duration.ofSeconds(15),
    polling: Duration = Duration.ofMillis(500),
    fw: FluentWait<WebDriver>? = null
): T {
    WaitElement(this.driver, this.xpath).waitTextPresent(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitExists(
    timeOut: Duration = Duration.ofSeconds(15),
    polling: Duration = Duration.ofMillis(500),
    fw: FluentWait<WebDriver>? = null
): T {
    WaitElement(this.driver, this.xpath).waitExists(timeOut, polling, fw)
    return this
}


fun <T : CustomElement<T>> T.waitDisplay(
    timeOut: Duration = Duration.ofSeconds(15),
    polling: Duration = Duration.ofMillis(500),
    fw: FluentWait<WebDriver>? = null
): T {
    WaitElement(this.driver, this.xpath).waitDisplay(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitClickable(
    timeOut: Duration = Duration.ofSeconds(15),
    polling: Duration = Duration.ofMillis(500),
    fw: FluentWait<WebDriver>? = null
): T {
    WaitElement(this.driver, this.xpath).waitClickable(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitInvisible(
    timeOut: Duration = Duration.ofSeconds(15),
    polling: Duration = Duration.ofMillis(500),
    fw: FluentWait<WebDriver>? = null
): T {
    WaitElement(this.driver, this.xpath).waitInvisible(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitNotExists(
    timeOut: Duration = Duration.ofSeconds(15),
    polling: Duration = Duration.ofMillis(500),
    fw: FluentWait<WebDriver>? = null
): T {
    WaitElement(this.driver, this.xpath).waitNotExists(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitEnabled(
    timeOut: Duration = Duration.ofSeconds(15),
    polling: Duration = Duration.ofMillis(500),
    fw: FluentWait<WebDriver>? = null
): T {
    WaitElement(this.driver, this.xpath).waitEnabled(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitDisabled(
    timeOut: Duration = Duration.ofSeconds(15),
    polling: Duration = Duration.ofMillis(500),
    fw: FluentWait<WebDriver>? = null
): T {
    WaitElement(this.driver, this.xpath).waitDisabled(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitCustomCondition(
    timeOut: Duration = Duration.ofSeconds(15),
    polling: Duration = Duration.ofMillis(500),
    condition: (element: T) -> Boolean
): T {
    WaitElement(this.driver, this.xpath).waitCustomCondition(timeOut, polling) { condition(this) }
    return this
}

fun <T : CustomElement<T>> T.sleep(time: Duration = Duration.ofMillis(150)): T {
    Thread.sleep(time.toMillis())
    return this
}