package io.github.vulka.impl.vulcan.hebe.login

data class PfxRequest(
    val OS: String,
    val deviceModel: String,
    val certificate: String,
    val certificateType: String,
    val certificateThumbprint: String,
    val pin: String,
    val securityToken: String,
    val selfIdentifier: String
)
