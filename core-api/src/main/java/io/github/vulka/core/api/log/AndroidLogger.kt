package io.github.vulka.core.api.log

import android.util.Log

class AndroidLogger(clazz: Class<*>) : Logger(clazz) {
    override fun info(message: String) {
        Log.i(clazz.simpleName,message)
    }

    override fun debug(message: String) {
        Log.d(clazz.simpleName,message)
    }

    override fun warn(message: String) {
        Log.w(clazz.simpleName,message)
    }

    override fun error(message: String) {
        Log.d(clazz.simpleName,message)
    }
}