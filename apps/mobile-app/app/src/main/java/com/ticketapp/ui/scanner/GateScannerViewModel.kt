package com.ticketapp.ui.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ticketapp.data.model.CheckInResponseDto
import com.ticketapp.domain.repository.GateRepository

import com.ticketapp.domain.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

sealed interface ScanUiState {
    object ReadyToScan : ScanUiState
    object CheckingIn : ScanUiState
    data class ScanSuccess(val result: CheckInResponseDto) : ScanUiState
    data class ScanFailed(val message: String, val errorCode: String?) : ScanUiState
}

class GateScannerViewModel(
    private val repository: GateRepository
) : ViewModel() {

    private val _scanState = MutableStateFlow<ScanUiState>(ScanUiState.ReadyToScan)
    val scanState: StateFlow<ScanUiState> = _scanState.asStateFlow()

    private var lastScannedCode: String? = null
    private var lastScanTimestamp: Long = 0L
    private val DEBOUNCE_INTERVAL_MS = 2000L

    fun onQrScanned(rawPayload: String, gateId: String) {
        val now = System.currentTimeMillis()
        if (rawPayload == lastScannedCode && (now - lastScanTimestamp) < DEBOUNCE_INTERVAL_MS) {
            return
        }

        lastScannedCode = rawPayload
        lastScanTimestamp = now

        val parts = rawPayload.split(":")
        if (parts.size != 2) {
            _scanState.value = ScanUiState.ScanFailed("Mã QR không đúng định dạng sự kiện", "INVALID_FORMAT")
            return
        }

        val ticketUuid = try { UUID.fromString(parts[0]) } catch (e: Exception) { null }
        val totpCode = parts[1]

        if (ticketUuid == null || totpCode.length != 6) {
            _scanState.value = ScanUiState.ScanFailed("Dữ liệu vé bị biến dạng hoặc mã TOTP sai độ dài", "MALFORMED_DATA")
            return
        }

        processCheckIn(ticketUuid, totpCode, gateId)
    }

    private fun processCheckIn(ticketCode: UUID, totpCode: String, gateId: String) {
        viewModelScope.launch {
            _scanState.value = ScanUiState.CheckingIn
            when (val result = repository.checkIn(ticketCode, totpCode, gateId)) {
                is NetworkResult.Success -> {
                    _scanState.value = ScanUiState.ScanSuccess(result.data)
                }
                is NetworkResult.Error -> {
                    _scanState.value = ScanUiState.ScanFailed(result.message, result.errorCode)
                }
                is NetworkResult.Exception -> {
                    _scanState.value = ScanUiState.ScanFailed("Mất kết nối mạng tới Server cổng", "NETWORK_ERROR")
                }
            }
        }
    }

    fun resetScanner() {
        _scanState.value = ScanUiState.ReadyToScan
        lastScannedCode = null
    }
}