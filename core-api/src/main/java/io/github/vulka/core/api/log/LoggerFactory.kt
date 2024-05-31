package io.github.vulka.core.api.log

object LoggerFactory {
    fun get(clazz: Class<*>): Logger {
        return AndroidLogger(clazz)
    }
}