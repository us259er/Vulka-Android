package io.github.vulka.impl.vulcan.hebe.types

import java.util.UUID

data class Payload(
    var appName: String,
    var appVersion: String,
    var certificateId: String,
    var envelope: Any,
    var firebaseToken: String,
    var api: Int,
    var requestId: UUID,
    var timestamp: Long,
    var timestampFormatted: String
)
