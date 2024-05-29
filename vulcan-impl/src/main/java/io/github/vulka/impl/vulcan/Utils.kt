package io.github.vulka.impl.vulcan

import java.util.UUID

object Utils {
    @JvmStatic
    fun uuid(seed: String?): String {
        return if (!seed.isNullOrEmpty()) {
            UUID.nameUUIDFromBytes(seed.toByteArray()).toString()
        } else {
            UUID.randomUUID().toString()
        }
    }
}
