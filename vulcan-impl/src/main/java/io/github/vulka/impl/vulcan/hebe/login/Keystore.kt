package io.github.vulka.impl.vulcan.hebe.login

import io.github.wulkanowy.signer.hebe.generateCertificate
import io.github.wulkanowy.signer.hebe.generateKeyPair
import org.slf4j.Logger
import org.slf4j.LoggerFactory

data class Keystore(
    val certificate: String,
    val fingerprint: String,
    val privateKey: String,
    val firebaseToken: String,
    val deviceModel: String
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(Keystore::class.java)

        @JvmStatic
        @Throws(Exception::class)
        fun create(firebaseToken: String?, deviceModel: String): Keystore {
            logger.debug("Generating key pair...")
            val (_, privatePem, _) = generateKeyPair()
            val (certificatePem, certificateHash) = generateCertificate(privatePem)

            val token = firebaseToken ?: ""

            val keystore = Keystore(
                certificate = certificatePem,
                fingerprint = certificateHash,
                privateKey = privatePem,
                firebaseToken = token,
                deviceModel = deviceModel
            )

            logger.debug("Generated for $deviceModel, sha1: ${keystore.fingerprint}")
            return keystore
        }

    }
}
