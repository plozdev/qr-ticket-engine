package com.ticketapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun ScannerOverlay(
    modifier: Modifier = Modifier,
    boxSizeDp: Float = 260f
) {
    Canvas(modifier = modifier) {
        val boxPx = boxSizeDp.dp.toPx()
        val left = (size.width - boxPx) / 2
        val top = (size.height - boxPx) / 2

        drawRect(color = Color.Black.copy(alpha = 0.6f))

        drawRoundRect(
            color = Color.Transparent,
            topLeft = Offset(left, top),
            size = Size(boxPx, boxPx),
            cornerRadius = CornerRadius(16.dp.toPx()),
            blendMode = BlendMode.Clear
        )

        drawRoundRect(
            color = Color(0xFFA50064),
            topLeft = Offset(left, top),
            size = Size(boxPx, boxPx),
            cornerRadius = CornerRadius(16.dp.toPx()),
            style = Stroke(width = 4.dp.toPx())
        )
    }
}
