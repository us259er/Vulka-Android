package io.github.vulka.impl.vulcan

open class VulcanHebeException(message: String) : Exception(message)

class InvalidPINException : VulcanHebeException("Invalid PIN")
class ExpiredTokenException : VulcanHebeException("Expired Token")
class InvalidSymbolException : VulcanHebeException("Invalid Symbol")
class InvalidTokenException : VulcanHebeException("Invalid Token")
class UnauthorizedCertificateException : VulcanHebeException("Unauthorized Certificate")