package com.ticketapp.ui.attendee

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ticketapp.core.qrcode.QrCodeGenerator
import com.ticketapp.domain.repository.TicketRepository
import com.ticketapp.domain.util.NetworkResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

sealed interface QrUiState {
    object Idle : QrUiState
    object Loading : QrUiState
    data class Success(
        val qrBitmap: Bitmap,
        val totpCode: String,
        val remainingSeconds: Int,
        val totalSeconds: Int = 30
    ) : QrUiState
    data class Error(val message: String) : QrUiState
}

class AttendeeTicketViewModel (
    private val repository: TicketRepository,
    private val qrCodeGenerator: QrCodeGenerator
) : ViewModel() {
    private val _uiState = MutableStateFlow<QrUiState>(QrUiState.Idle)
    val uiState: StateFlow<QrUiState> = _uiState.asStateFlow()
    private var countdownJob: Job? = null
    private var currentTicketId: Long? = null
    private var currentUserId: String? = null

    fun loadTicketQr(ticketId: Long, userId: String) {
        currentTicketId = ticketId
        currentUserId = userId
        fetchQrCode(ticketId, userId)
    }
    private fun fetchQrCode(ticketId: Long, userId: String) {
        viewModelScope.launch {
            _uiState.value = QrUiState.Loading
            when (val result = repository.getDynamicQr(ticketId, userId)) {
                is NetworkResult.Success -> {
                    val payload = result.data
                    val bitmap = qrCodeGenerator.generateQrBitmap(payload.payloadString, 512)

                    if (bitmap != null)
                        startCountdownTimer(payload.remainingSeconds, bitmap, payload.totpCode)
                    else
                        _uiState.value = QrUiState.Error("Không thể tạo mã QR. Vui lòng thử lại!")
                }
                is NetworkResult.Error -> {
                    _uiState.value = QrUiState.Error(result.message)
                }
                is NetworkResult.Exception -> {
                    _uiState.value = QrUiState.Error("Mất kết nối tới máy chủ. Vui lòng kiểm tra mạng.")
                }
            }
        }
    }

    private fun startCountdownTimer(initialSeconds: Int, bitmap: Bitmap, totpCode: String) {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            var secondsLeft = initialSeconds
            while (secondsLeft > 0) {
                _uiState.value = QrUiState.Success(
                    qrBitmap = bitmap,
                    totpCode = totpCode,
                    remainingSeconds = secondsLeft
                )
                delay(1000L.milliseconds)
                secondsLeft--
            }
            currentTicketId?.let { ticketId ->
                currentUserId?.let { userId -> fetchQrCode(ticketId, userId) }
            }
        }
    }

    @SuppressLint("EmptySuperCall")
    override fun onCleared() {
        super.onCleared()
        countdownJob?.cancel()
    }
}