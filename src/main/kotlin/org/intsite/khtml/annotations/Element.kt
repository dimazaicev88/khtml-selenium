package org.intsite.khtml.annotations

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Element(val xpath: String)