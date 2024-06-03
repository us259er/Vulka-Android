package io.github.vulka.ui.crypto

import com.google.gson.Gson
import dev.medzik.android.crypto.KeyStore
import dev.medzik.android.crypto.KeyStoreAlias
import dev.medzik.libcrypto.Hex
import io.github.vulka.core.api.LoginCredentials
import io.github.vulka.impl.librus.LibrusLoginCredentials
import io.github.vulka.impl.vulcan.VulcanLoginCredentials

object CredentialsKeyStore : KeyStoreAlias {
    override val name: String = "credentials"
}

fun serializeCredentialsAndEncrypt(response: LoginCredentials): String {
    val json = Gson().toJson(
        when (response) {
            is VulcanLoginCredentials -> response
            is LibrusLoginCredentials -> response.request
            else -> throw IllegalStateException()
        }
    )

    val cipherEnc = KeyStore.initForEncryption(CredentialsKeyStore, false)
    val cipherData = KeyStore.encrypt(cipherEnc, json.toByteArray())

    return cipherData.initializationVector + cipherData.cipherText
}

// TODO: maybe move somewhere else
fun serializeCredentials(response: LoginCredentials): String {
    return Gson().toJson(
        when (response) {
            is VulcanLoginCredentials -> response
            is LibrusLoginCredentials -> response.request
            else -> throw IllegalStateException()
        }
    )
}

fun decryptCredentials(cipherData: String): String {
    // initialization vector length in hex string
    val ivLength = 12 * 2

    // extract IV and Cipher Text from hex string
    val iv = cipherData.substring(0, ivLength)
    val cipherText = cipherData.substring(ivLength)

    // decrypt cipher text
    val cipher = KeyStore.initForDecryption(CredentialsKeyStore, Hex.decode(iv), false)
    val decrypted = KeyStore.decrypt(cipher, cipherText)

    return String(decrypted)
}
