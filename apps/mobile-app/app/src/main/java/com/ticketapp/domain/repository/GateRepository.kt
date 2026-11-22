package com.ticketapp.domain.repository

import com.ticketapp.data.model.CheckInResponseDto
import com.ticketapp.domain.util.NetworkResult
import java.util.UUID

interface GateRepository {
    suspend fun checkIn(ticketCode: UUID, totpCode: String, gateId: String): NetworkResult<CheckInResponseDto>

}