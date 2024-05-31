package io.github.vulka.core.api

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION,
    AnnotationTarget.TYPE_PARAMETER, AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.EXPRESSION)
annotation class LibrusOnly
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION,
    AnnotationTarget.TYPE_PARAMETER, AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.EXPRESSION)
annotation class VulcanHebeOnly