package io.github.vulka.impl.vulcan.hebe.login

import android.content.Context
import android.util.Log
import io.github.vulka.impl.vulcan.hebe.generateKeyPair
import io.github.vulka.impl.vulcan.hebe.getKeyEntry
import java.security.PrivateKey
import kotlin.random.Random

data class HebeKeystore(
    val privateKeyAlias: String,
    val firebaseToken: String,
    val deviceModel: String
) {

    fun getData(): Triple<String,String,PrivateKey> {
        return getKeyEntry(privateKeyAlias)
    }

    companion object {

        fun generateKeystoreName(symbol: String): String {
            // Key name must be random, without login we don't have any information about account
            return "vulcan_hebe_key_$symbol-${Random.nextInt()}"
        }

        @Throws(Exception::class)
        fun restore(alias: String, firebaseToken: String?, deviceModel: String): HebeKeystore {
            Log.d("Vulcan Keystore","Restoring key pair...")

            val token = firebaseToken ?: ""

            val keystore = HebeKeystore(
                privateKeyAlias = alias,
                firebaseToken = token,
                deviceModel = deviceModel
            )
            val (_, fingerprint, _) = keystore.getData()

            Log.d("Vulcan Keystore","Generated for $deviceModel, sha1: $fingerprint")
            return keystore
        }

        @Throws(Exception::class)
        fun create(context: Context, alias: String, firebaseToken: String?, deviceModel: String): HebeKeystore {
            Log.d("Vulcan Keystore","Generating key pair...")
            val (_, fingerprint, _) = generateKeyPair(context, alias)

            val token = firebaseToken ?: ""

            val keystore = HebeKeystore(
                privateKeyAlias = alias,
                firebaseToken = token,
                deviceModel = deviceModel
            )

            Log.d("Vulcan Keystore","Generated for $deviceModel, sha1: $fingerprint")
            return keystore
        }

    }
}
