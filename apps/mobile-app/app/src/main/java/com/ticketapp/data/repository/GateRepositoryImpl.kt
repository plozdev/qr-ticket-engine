package com.ticketapp.data.repository

import com.google.gson.Gson
import com.ticketapp.data.api.GateApiService
import com.ticketapp.data.model.CheckInRequestDto
import com.ticketapp.data.model.CheckInResponseDto
import com.ticketapp.domain.repository.GateRepository
import com.ticketapp.domain.util.NetworkResult
import java.util.UUID

class GateRepositoryImpl (
    private val gateApi: GateApiService,
    private val gson: Gson
): BaseRepository(gson),  GateRepository {
    override suspend fun checkIn(
        ticketCode: UUID,
        totpCode: String,
        gateId: String
    ): NetworkResult<CheckInResponseDto> {
        val request = CheckInRequestDto(ticketCode, totpCode, gateId)
        return safeApiCall { gateApi.checkInTicket(request) }
    }
}