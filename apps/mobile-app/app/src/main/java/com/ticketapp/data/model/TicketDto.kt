package com.ticketapp.data.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class TicketDto(
    @SerializedName("id") val id: Long,
    @SerializedName("ticketCode") val ticketCode: UUID,
    @SerializedName("userId") val userId: String,
    @SerializedName("ownerName") val ownerName: String,
    @SerializedName("status") val status: String, // ISSUED | CHECKED_IN | REVOKED
    @SerializedName("checkedInGate") val checkedInGate: String?,
    @SerializedName("checkedInAt") val checkedInAt: String?
)
