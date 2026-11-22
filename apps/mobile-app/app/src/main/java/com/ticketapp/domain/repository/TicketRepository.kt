package com.ticketapp.domain.repository

import com.ticketapp.data.model.QrPayloadDto
import com.ticketapp.data.model.TicketDto
import com.ticketapp.domain.util.NetworkResult

interface TicketRepository {
    suspend fun getUserTickets(userId : String) : NetworkResult<List<TicketDto>>
    suspend fun getDynamicQr(ticketId: Long, userId: String): NetworkResult<QrPayloadDto>
}