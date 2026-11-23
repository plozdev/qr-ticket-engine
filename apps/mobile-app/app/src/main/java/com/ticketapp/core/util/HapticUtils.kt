package com.ticketapp.core.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

object HapticUtils {

    @Suppress("DEPRECATION")
    private fun getVibrator(context: Context): Vibrator {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    /**
     * Rung 1 nhịp êm (100ms) khi vé hợp lệ (Success)
     */
    fun triggerSuccess(context: Context) {
        val vibrator = getVibrator(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(100)
        }
    }

    /**
     * Rung kép dồn dập (nghỉ 0ms, rung 150ms, nghỉ 100ms, rung 250ms) cảnh báo vé lỗi / vé lậu (Error/Conflict)
     */
    fun triggerError(context: Context) {
        val vibrator = getVibrator(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val timings = longArrayOf(0, 150, 100, 250)
            vibrator.vibrate(VibrationEffect.createWaveform(timings, -1))
        } else {
            val timings = longArrayOf(0, 150, 100, 250)
            vibrator.vibrate(timings, -1)
        }
    }
}
