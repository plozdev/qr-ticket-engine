package com.ticketapp.data.api

import com.ticketapp.data.model.CheckInRequestDto
import com.ticketapp.data.model.CheckInResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GateApiService {
    @POST("/api/gates/check-in")
    suspend fun checkInTicket(
        @Body request: CheckInRequestDto
    ): Response<CheckInResponseDto>
}