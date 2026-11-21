package com.ticketapp.data.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class CheckInRequestDto (
    @SerializedName("ticketCode") val ticketCode: UUID,
    @SerializedName("totpCode") val totpCode: String,
    @SerializedName("gateId") val gateId: String
)