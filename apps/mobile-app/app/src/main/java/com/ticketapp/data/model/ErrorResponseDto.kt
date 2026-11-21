package com.ticketapp.data.model

import com.google.gson.annotations.SerializedName

data class ErrorResponseDto(
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("status") val status: Int,
    @SerializedName("errorCode") val errorCode: String,
    @SerializedName("message") val message: String
)
