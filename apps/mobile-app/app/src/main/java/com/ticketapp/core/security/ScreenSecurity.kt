package com.ticketapp.core.security

import android.app.Activity
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun EnableScreenSecurityAndBrightness() {
    val context = LocalContext.current
    val activity = context as? Activity ?: return

    DisposableEffect(Unit) {
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        val layoutParams = activity.window.attributes
        val originalBrightness = layoutParams.screenBrightness
        layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
        activity.window.attributes = layoutParams

        onDispose {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
            layoutParams.screenBrightness = originalBrightness
            activity.window.attributes = layoutParams
        }
    }
}
