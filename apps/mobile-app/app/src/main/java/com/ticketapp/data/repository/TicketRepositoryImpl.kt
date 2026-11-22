package com.ticketapp.data.repository

import com.google.gson.Gson
import com.ticketapp.data.api.TicketApiService
import com.ticketapp.data.model.QrPayloadDto
import com.ticketapp.data.model.TicketDto
import com.ticketapp.domain.repository.TicketRepository
import com.ticketapp.domain.util.NetworkResult

class TicketRepositoryImpl (
    private val ticketApi: TicketApiService,
    private val gson: Gson
) : BaseRepository(gson), TicketRepository {

    override suspend fun getUserTickets(userId: String): NetworkResult<List<TicketDto>> {
        return safeApiCall { ticketApi.getUserTickets(userId) }
    }

    override suspend fun getDynamicQr(
        ticketId: Long,
        userId: String
    ): NetworkResult<QrPayloadDto> {
        return safeApiCall { ticketApi.getDynamicQr(ticketId, userId) }
    }
}