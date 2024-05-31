package io.github.vulka.impl.vulcan.hebe.login

import com.google.gson.annotations.SerializedName

data class PfxRequest(
    @SerializedName("OS")
    val os: String,
    @SerializedName("deviceModel")
    val deviceModel: String,
    @SerializedName("certificate")
    val certificate: String,
    @SerializedName("certificateType")
    val certificateType: String,
    @SerializedName("certificateThumbprint")
    val certificateThumbprint: String,
    @SerializedName("pin")
    val pin: String,
    @SerializedName("securityToken")
    val securityToken: String,
    @SerializedName("selfIdentifier")
    val selfIdentifier: String
)
