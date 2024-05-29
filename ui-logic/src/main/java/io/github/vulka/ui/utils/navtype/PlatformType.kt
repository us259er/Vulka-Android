package io.github.vulka.ui.utils.navtype

import android.os.Bundle
import androidx.navigation.NavType
import io.github.vulka.core.api.Platform

val PlatformType = object : NavType<Platform>(
    isNullableAllowed = false
) {
    override fun get(bundle: Bundle, key: String): Platform {
        val ordinal = bundle.getInt(key)
        return Platform.entries[ordinal]
    }

    override fun parseValue(value: String): Platform {
        return Platform.entries[value.toInt()]
    }

    override fun serializeAsValue(value: Platform): String {
        return value.ordinal.toString()
    }

    override fun put(bundle: Bundle, key: String, value: Platform) {
        bundle.putInt(key, value.ordinal)
    }
}
