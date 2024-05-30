package io.github.vulka.impl.vulcan.hebe

import android.content.Context
import android.security.KeyPairGeneratorSpec
import com.migcomponents.migbase64.Base64
import java.math.BigInteger
import java.net.URLEncoder
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.text.SimpleDateFormat
import java.util.*
import javax.security.auth.x500.X500Principal
import java.security.MessageDigest.getInstance as createSign

// Code from: https://github.com/wulkanowy/uonet-request-signer/tree/master/hebe-android

private fun getDigest(body: String?): String {
    if (body == null) return ""
    return Base64.encodeToString(createSign("SHA-256").digest(body.toByteArray()), false)
}

private fun getSignatureValue(values: String, privateKey: PrivateKey): String {
    val signature = Signature.getInstance("SHA256withRSA")
    signature.initSign(privateKey)
    signature.update(values.toByteArray())

    return Base64.encodeToString(signature.sign(), false)
}

private fun getEncodedPath(path: String): String {
    val url = ("(api/mobile/.+)".toRegex().find(path))
        ?: throw IllegalArgumentException("The URL does not seem correct (does not match `(api/mobile/.+)` regex)")

    return URLEncoder.encode(url.groupValues[0], "UTF-8").orEmpty().lowercase()
}

private fun getHeadersList(body: String?, digest: String, canonicalUrl: String, timestamp: Date): Pair<String, String> {
    val signData = mutableMapOf<String, String>()
    signData["vCanonicalUrl"] = canonicalUrl
    if (body != null) signData["Digest"] = digest
    signData["vDate"] = SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss", Locale.ENGLISH).apply {
        timeZone = TimeZone.getTimeZone("GMT")
    }.format(timestamp) + " GMT"

    return Pair(
        first = signData.keys.joinToString(" "),
        second = signData.values.joinToString("")
    )
}

fun getSignatureValues(
    fingerprint: String,
    privateKey: String,
    body: String?,
    fullUrl: String,
    timestamp: Date
): Triple<String, String, String> {
    val keySpec = PKCS8EncodedKeySpec(Base64.decode(privateKey))
    val keyFactory = KeyFactory.getInstance("RSA")
    val key = keyFactory.generatePrivate(keySpec)
    return getSignatureValues(fingerprint, key, body, fullUrl, timestamp)
}

fun getSignatureValues(
    fingerprint: String,
    privateKey: PrivateKey,
    body: String?,
    fullUrl: String,
    timestamp: Date
): Triple<String, String, String> {
    val canonicalUrl = getEncodedPath(fullUrl)
    val digest = getDigest(body)
    val (headers, values) = getHeadersList(body, digest, canonicalUrl, timestamp)
    val signatureValue = getSignatureValue(values, privateKey)

    return Triple(
        "SHA-256=${digest}",
        canonicalUrl,
        """keyId="$fingerprint",headers="$headers",algorithm="sha256withrsa",signature=Base64(SHA256withRSA($signatureValue))"""
    )
}

fun generateKeyPair(context: Context, alias: String): Triple<String, String, PrivateKey> {
    val now = System.currentTimeMillis()
    val notBefore = Date(now)

    val name = X500Principal("CN=APP_CERTIFICATE CA Certificate")

    val notAfter = Calendar.getInstance()
    notAfter.time = notBefore
    notAfter.add(Calendar.YEAR, 20)

    val genSpec = KeyPairGeneratorSpec.Builder(context)
        .setAlias(alias)
        .setSubject(name)
        .setSerialNumber(BigInteger.ONE)
        .setStartDate(notBefore)
        .setEndDate(notAfter.time)
        .build()

    val generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore")
    generator.initialize(genSpec)
    generator.generateKeyPair()

    return getKeyEntry(alias)
}

fun getKeyEntry(alias: String): Triple<String, String, PrivateKey> {
    val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }
    val entry = keyStore.getEntry(alias, null) as KeyStore.PrivateKeyEntry
    val cert = entry.certificate
    val privateKey = entry.privateKey

    val certificatePem = Base64.encodeToString(cert.encoded, false)
    val fingerprint = createSign("SHA-1")
        .digest(cert.encoded)
        .joinToString("") { "%02x".format(it) }
    return Triple(certificatePem, fingerprint, privateKey)
}