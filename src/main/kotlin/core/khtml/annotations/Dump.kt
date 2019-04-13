package core.khtml.annotations

import core.khtml.dump.DumpCondition

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Dump(
    val screenshot: Boolean = true,
    val condition: DumpCondition = DumpCondition.FAIL,
    val dirDump: String = ""
)