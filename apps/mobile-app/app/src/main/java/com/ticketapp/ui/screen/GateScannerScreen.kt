package com.ticketapp.ui.screen

import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.ticketapp.core.camera.startCameraX
import com.ticketapp.core.util.HapticUtils
import com.ticketapp.ui.components.CheckInStatusBanner
import com.ticketapp.ui.components.ScannerOverlay
import com.ticketapp.ui.scanner.GateScannerViewModel
import com.ticketapp.ui.scanner.ScanUiState

@Composable
fun GateScannerScreen(
    viewModel: GateScannerViewModel,
    gateId: String = "GATE_A_01"
) {
    val scanState by viewModel.scanState.collectAsState()
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                startCameraX(ctx, previewView) { rawQr ->
                    viewModel.onQrScanned(rawQr, gateId)
                }
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        ScannerOverlay(modifier = Modifier.fillMaxSize())

        when (val state = scanState) {
            is ScanUiState.ReadyToScan -> {}
            is ScanUiState.CheckingIn -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is ScanUiState.ScanSuccess -> {
                HapticUtils.triggerSuccess(context)
                CheckInStatusBanner(
                    isSuccess = true,
                    title = "HỢP LỆ - CỔNG ĐÃ MỞ",
                    detail = "Khách: ${state.result.ticketOwnerName}\nVé: ${state.result.ticketCode}",
                    onDismiss = { viewModel.resetScanner() },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
            is ScanUiState.ScanFailed -> {
                HapticUtils.triggerError(context)
                CheckInStatusBanner(
                    isSuccess = false,
                    title = "TỪ CHỐI QUA CỔNG!",
                    detail = state.message,
                    onDismiss = { viewModel.resetScanner() },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}
