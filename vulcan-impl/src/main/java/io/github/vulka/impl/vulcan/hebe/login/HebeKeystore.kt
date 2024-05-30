package io.github.vulka.impl.vulcan.hebe.login

import android.content.Context
import android.util.Log
import io.github.vulka.impl.vulcan.hebe.generateKeyPair
import io.github.vulka.impl.vulcan.hebe.getKeyEntry
import java.security.PrivateKey

data class HebeKeystore(
    val certificate: String,
    val fingerprint: String,
    val privateKey: PrivateKey,
    val firebaseToken: String,
    val deviceModel: String
) {

    companion object {
        @JvmStatic
        @Throws(Exception::class)
        fun restore(alias: String, firebaseToken: String?, deviceModel: String): HebeKeystore {
            Log.d("Vulcan Keystore","Restoring key pair...")
            val (certificate, fingerprint, privateKey) = getKeyEntry(alias)

            val token = firebaseToken ?: ""

            val keystore = HebeKeystore(
                certificate = certificate,
                fingerprint = fingerprint,
                privateKey = privateKey,
                firebaseToken = token,
                deviceModel = deviceModel
            )

            Log.d("Vulcan Keystore","Generated for $deviceModel, sha1: ${keystore.fingerprint}")
            return keystore
        }

        @JvmStatic
        @Throws(Exception::class)
        fun create(context: Context, alias: String, firebaseToken: String?, deviceModel: String): HebeKeystore {
            Log.d("Vulcan Keystore","Generating key pair...")
            val (certificate, fingerprint, privateKey) = generateKeyPair(context, alias)

            val token = firebaseToken ?: ""

            val keystore = HebeKeystore(
                certificate = certificate,
                fingerprint = fingerprint,
                privateKey = privateKey,
                firebaseToken = token,
                deviceModel = deviceModel
            )

            Log.d("Vulcan Keystore","Generated for $deviceModel, sha1: ${keystore.fingerprint}")
            return keystore
        }

    }
}
