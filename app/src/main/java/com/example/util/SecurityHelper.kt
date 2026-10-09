package com.example.util

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp

object SecurityHelper {
    /**
     * Enables or disables FLAG_SECURE on the current window.
     * When enabled, Android OS blocks screenshots, screen recording, and shows a black screen in recents.
     */
    fun setScreenSecure(activity: Activity?, secure: Boolean) {
        activity?.window?.let { window ->
            if (secure) {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE
                )
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }
        }
    }
}

/**
 * Composable effect to automatically protect sensitive screens (Study Materials, Video Player, Tests)
 * with WindowManager FLAG_SECURE while active, and cleanly restore on disposal.
 */
@Composable
fun EnforceScreenSecurity(enabled: Boolean = true) {
    val context = LocalContext.current
    val activity = context as? Activity

    DisposableEffect(enabled) {
        if (enabled && activity != null) {
            SecurityHelper.setScreenSecure(activity, true)
        }
        onDispose {
            if (enabled && activity != null) {
                // Return to normal window behavior when user leaves secure screens
                SecurityHelper.setScreenSecure(activity, false)
            }
        }
    }
}

/**
 * Dynamic Anti-Piracy Watermark overlay.
 * Renders diagonal floating watermark text containing student details across the canvas.
 */
@Composable
fun DynamicWatermarkOverlay(
    studentName: String,
    studentMobile: String,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val watermarkText = "Teacher Vision Academy  •  ${if (studentName.isBlank()) "Student" else studentName}  •  ${if (studentMobile.isBlank()) "7888213000" else studentMobile}"

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Render repeating diagonal watermark pattern
            val textLayoutResult = textMeasurer.measure(
                text = watermarkText,
                style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black.copy(alpha = 0.08f)
                )
            )

            val textWidth = textLayoutResult.size.width
            val textHeight = textLayoutResult.size.height

            rotate(degrees = -30f, pivot = Offset(width / 2f, height / 2f)) {
                var y = -height
                while (y < height * 2) {
                    var x = -width
                    while (x < width * 2) {
                        drawText(
                            textLayoutResult = textLayoutResult,
                            topLeft = Offset(x, y)
                        )
                        x += (textWidth + 120)
                    }
                    y += (textHeight + 140)
                }
            }
        }
    }
}
