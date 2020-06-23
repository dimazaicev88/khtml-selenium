package khtml.annotations

import khtml.waits.WaitCondition

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class Wait(
        val time: Long = 30,
        val condition: WaitCondition,
        val polling: Long = 500
)
