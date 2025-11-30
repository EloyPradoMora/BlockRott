package com.example.blockrott.frontend.utils

import androidx.compose.ui.graphics.Color
import com.example.blockrott.frontend.theme.Discord
import com.example.blockrott.frontend.theme.Facebook
import com.example.blockrott.frontend.theme.Instagram
import com.example.blockrott.frontend.theme.Reddit
import com.example.blockrott.frontend.theme.TikTok
import com.example.blockrott.frontend.theme.TimerBackground
import com.example.blockrott.frontend.theme.Youtube

class FrontendUtils {
    fun totalMinutes(cadena: String): Double {
        var minutosTotales = 0
        val partes = cadena.split(" ")

        for (parte in partes) {
            if (parte.endsWith("h")) {
                val horas = parte.replace("h", "").toIntOrNull() ?: 0
                minutosTotales += horas * 60
            } else if (parte.endsWith("min") || parte.endsWith("m")) {
                val minutos = parte
                    .replace("min", "")
                    .replace("m", "")
                    .toIntOrNull() ?: 0
                minutosTotales += minutos
            }
        }
        return minutosTotales.toDouble()
    }

    fun colorApp(appName: String): Color {
        val color: Color = if (appName.contains("Facebook")) {
            Facebook
        } else if (appName.contains("Instagram")){
            Instagram
        } else if (appName.contains("TikTok")){
            TikTok
        } else if (appName.contains("YouTube")){
            Youtube
        } else if (appName.contains("Discord")){
            Discord
        } else if (appName.contains("Reddit")){
            Reddit
        } else{
            TimerBackground
        }
        return color
    }
}