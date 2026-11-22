package com.ticketapp.data.repository

import com.google.gson.Gson
import com.ticketapp.data.model.ErrorResponseDto
import com.ticketapp.domain.util.NetworkResult
import retrofit2.Response

abstract class BaseRepository (private val gson: Gson) {
    protected suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val parsedError = try {
                    gson.fromJson(errorBody, ErrorResponseDto::class.java)
                } catch (e: Exception) { null }

                NetworkResult.Error(
                    code = response.code(),
                    message = parsedError?.message ?: "Lỗi máy chủ (${response.code()})",
                    errorCode = parsedError?.errorCode
                )
            }
        } catch (e: Throwable) {
            NetworkResult.Exception(e)
        }
    }
}