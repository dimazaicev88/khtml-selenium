package core.khtml.annotations

import core.khtml.waits.WaitCondition

@Target(AnnotationTarget.FUNCTION)
annotation class Wait(
    val time: Long = 30,
    val condition: WaitCondition,
    val polling: Long = 500
)
