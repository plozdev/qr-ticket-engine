package com.ticketapp.data.model

import com.google.gson.annotations.SerializedName

data class CheckInResponseDto(
    @SerializedName("status") val status: String,
    @SerializedName("ticketCode") val ticketCode: String,
    @SerializedName("ticketOwnerName") val ticketOwnerName: String,
    @SerializedName("gateId") val gateId: String,
    @SerializedName("scanTime") val scanTime: String,
    @SerializedName("msg") val msg: String
)
