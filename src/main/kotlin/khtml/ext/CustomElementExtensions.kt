package khtml.ext

import khtml.element.CustomElement
import khtml.waits.WaitElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.FluentWait


fun <T : CustomElement<T>> T.waitAjax(timeOut: Long = 5, polling: Long = 10, fw: FluentWait<WebDriver>? = null): T {
        WaitElement(this.driver, this.xpath).waitAjax(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitTextPresent(
        timeOut: Long = 5,
        polling: Long = 200,
        fw: FluentWait<WebDriver>? = null
): T {
        WaitElement(this.driver, this.xpath).waitTextPresent(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitDisplay(timeOut: Long = 5, polling: Long = 100, fw: FluentWait<WebDriver>? = null): T {
        WaitElement(this.driver, this.xpath).waitDisplay(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitClickable(
        timeOut: Long = 5,
        polling: Long = 200,
        fw: FluentWait<WebDriver>? = null
): T {
    WaitElement(this.driver, this.xpath).waitClickable(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitInvisible(
        timeOut: Long = 5,
        polling: Long = 200,
        fw: FluentWait<WebDriver>? = null
): T {
    WaitElement(this.driver, this.xpath).waitInvisible(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitNotExists(
        timeOut: Long = 5,
        polling: Long = 200,
        fw: FluentWait<WebDriver>? = null
): T {
        WaitElement(this.driver, this.xpath).waitNotExists(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitEnabled(timeOut: Long = 5, polling: Long = 100, fw: FluentWait<WebDriver>? = null): T {
    WaitElement(this.driver, this.xpath).waitEnabled(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitDisabled(
        timeOut: Long = 5,
        polling: Long = 200,
        fw: FluentWait<WebDriver>? = null
): T {
    WaitElement(this.driver, this.xpath).waitDisabled(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitAllAjax(timeOut: Long = 5, polling: Long = 100, fw: FluentWait<WebDriver>? = null): T {
    WaitElement(this.driver, this.xpath).waitAllAjax(timeOut, polling, fw)
    return this
}

fun <T : CustomElement<T>> T.waitCustomCondition(
        timeOut: Long = 5,
        polling: Long = 100,
        condition: (xpath: String) -> Boolean
): T {
    WaitElement(this.driver, this.xpath).waitCustomCondition(timeOut, polling) { condition(xpath) }
    return this
}

fun <T : CustomElement<T>> T.sleep(time: Long = 150): T {
    Thread.sleep(time)
    return this
}