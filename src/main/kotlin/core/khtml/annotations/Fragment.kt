package core.khtml.annotations

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Fragment(val xpath: String, val inherited: Boolean = false)