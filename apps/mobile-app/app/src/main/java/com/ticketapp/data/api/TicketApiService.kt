package com.ticketapp.data.api

import com.ticketapp.data.model.QrPayloadDto
import com.ticketapp.data.model.TicketDto
import retrofit2.Response
import retrofit2.http.*

interface TicketApiService {
    @GET("/api/tickets/users/{userId}")
    suspend fun getUserTickets( @Path("userId") userId: String) : Response<List<TicketDto>>

    @GET("/api/tickets/{ticketId}/qr")
    suspend fun getDynamicQr(
        @Path("ticketId") ticketId: Long,
        @Header("X-User-Id") reqUserId: String
    ): Response<QrPayloadDto>
}