package com.example.blockrott.frontend.screens

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.blockrott.frontend.components.AppStatistics
import com.example.blockrott.frontend.components.StatisticsButton
import com.example.blockrott.frontend.components.UsageStats
import com.example.blockrott.frontend.theme.AppTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import backend.Usuario

@Composable
fun HomeScreen(){
    var context = LocalContext.current
    var showStatistics by remember { mutableStateOf(false) }
    var listaEstadisticas by remember { mutableStateOf(emptyList<UsageStats>()) }
    var tiempoTotal by remember { mutableStateOf("0m") }



    fun actualizarEstadisticas(){
        var tempUsuario = Usuario()
        if (!tempUsuario.verificarPermisos(context)) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
            return
        }

        var usuario = Usuario()
        usuario.agregarEspecificacionNueva("com.zhiliaoapp.musically",0, context)
        usuario.agregarEspecificacionNueva("com.google.android.youtube",0, context)
        usuario.agregarEspecificacionNueva("com.reddit.frontpage",0, context)
        usuario.agregarEspecificacionNueva("com.instagram.android",0, context)

        val rawUseTimeString = usuario.revisarTiempos()

        val listaTemporal: List<UsageStats> = rawUseTimeString.lines().filter { it.isNotBlank() }
            .mapNotNull { line ->
                val parts = line.split(',')
                if (parts.size == 2) {
                    UsageStats(appName = parts[0].trim(), usageTime = parts[1].trim())
                } else {
                    null
                }
            }

        val totalTiempo = calcularTiempoTotal(listaTemporal)

        listaEstadisticas = listaTemporal
        tiempoTotal = totalTiempo.toString() + "m"
        showStatistics = true
    }

    Scaffold(
        topBar = {},
        bottomBar = {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                StatisticsButton(
                    modifier = Modifier
                        .height(65.dp)
                        .width(125.dp),
                    onClick = {actualizarEstadisticas() }
                )
            }
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (showStatistics){
                AppStatistics(tiempoTotal, listaEstadisticas)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview(){
    AppTheme {
        HomeScreen()
    }
}

fun tiempoAMinutos(timeString: String): Int{
    var total = 0
    var horasMatch = "(\\d+)\\s*h".toRegex().find(timeString)
    val minutosMatch = "(\\d+)\\s*m".toRegex().find(timeString)
    horasMatch?.let {
        val horas = it.groupValues[1].toIntOrNull() ?: 0
        total += horas * 60
    }
    minutosMatch?.let {
        val minutes = it.groupValues[1].toIntOrNull() ?: 0
        total += minutes
    }
    return total
}

fun calcularTiempoTotal(listaTemporal: List<UsageStats>): Int {
    val tiempoEnMinutos = listaTemporal.sumOf { usageStat ->
        tiempoAMinutos(usageStat.usageTime)
    }
    return tiempoEnMinutos
}