package io.github.vulka.core.api.log

/**
 * Platform independent logger
 */
abstract class Logger(
    val clazz: Class<*>
) {

    abstract fun info(message: String)

    abstract fun debug(message: String)

    abstract fun warn(message: String)

    abstract fun error(message: String)
}