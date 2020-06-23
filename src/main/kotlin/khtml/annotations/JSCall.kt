package khtml.annotations

@Target(AnnotationTarget.FUNCTION)
annotation class JSCall(
        val funcName: String = "",
        val js: String = "",
        val async: Boolean = false
)