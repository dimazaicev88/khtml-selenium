package org.intsite.khtml.annotations

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Fragment(val xpath: String = "", val inheritance: Boolean = false)