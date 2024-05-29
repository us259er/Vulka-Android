package io.github.vulka.impl.vulcan

open class VulcanAPIException(message: String) : Exception(message)

class InvalidPINException : VulcanAPIException("Invalid PIN")
class ExpiredTokenException : VulcanAPIException("Expired Token")
class InvalidSymbolException : VulcanAPIException("Invalid Symbol")
class InvalidTokenException : VulcanAPIException("Invalid Token")
class UnauthorizedCertificateException : VulcanAPIException("Unauthorized Certificate")