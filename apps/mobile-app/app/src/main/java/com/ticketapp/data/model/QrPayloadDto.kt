package com.ticketapp.data.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class QrPayloadDto (
    @SerializedName("ticketCode") val ticketCode: UUID,
    @SerializedName("totpCode") val totpCode: String,
    @SerializedName("remainingSeconds") val remainingSeconds: Int,
    @SerializedName("payloadString") val payloadString: String // "UUID:TOTP"
)