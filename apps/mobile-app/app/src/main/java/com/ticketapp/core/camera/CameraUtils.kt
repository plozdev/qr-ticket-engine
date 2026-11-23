package com.ticketapp.core.camera

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.Executors

/**
 * Khởi động CameraX và gắn bộ phân tích mã QR (QrBarcodeAnalyzer) vào vòng đời của màn hình.
 */
fun startCameraX(
    context: Context,
    previewView: PreviewView,
    onQrDetected: (String) -> Unit
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    val executor = ContextCompat.getMainExecutor(context)
    val analysisExecutor = Executors.newSingleThreadExecutor()

    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        // 1. Luồng xem trước hình ảnh trên màn hình (Preview)
        val preview = Preview.Builder().build().also {
            it.surfaceProvider = previewView.surfaceProvider
        }

        // 2. Luồng phân tích hình ảnh (ImageAnalysis) chạy ngầm
        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(analysisExecutor, QrBarcodeAnalyzer(onQrDetected))
            }

        // 3. Chọn Camera sau (Back Camera)
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            // Hủy các ràng buộc cũ trước khi bind mới
            cameraProvider.unbindAll()

            // Gắn camera vào LifecycleOwner của màn hình
            val lifecycleOwner = context as? LifecycleOwner ?: return@addListener
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }, executor)
}
