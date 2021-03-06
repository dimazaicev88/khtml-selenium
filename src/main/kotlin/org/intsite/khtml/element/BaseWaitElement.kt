package org.intsite.khtml.element

import org.intsite.khtml.waits.WaitCondition


interface BaseWaitElement<T> {

    fun wait(condition: WaitCondition = WaitCondition.TO_CLICKABLE, timeOut: Long = 5): T

    fun waitCustomCondition(timeOut: Long = 5, condition: (xpath: String) -> Boolean): T
}