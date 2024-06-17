package io.github.vulka.impl.vulcan

open class VulcanAPIException(message: String) : Exception(message)

class InvalidPINException(message: String = "Invalid PIN") : VulcanAPIException(message)
class ExpiredTokenException(message: String = "Expired Token") : VulcanAPIException(message)
class InvalidSymbolException(message: String = "Invalid Symbol") : VulcanAPIException(message)
class InvalidTokenException(message: String = "Invalid Token") : VulcanAPIException(message)
class UnauthorizedCertificateException(message: String = "Unauthorized Certificate") : VulcanAPIException(message)
class InvalidSignatureValuesException(message: String = "Invalid Signature Values") : VulcanAPIException(message)