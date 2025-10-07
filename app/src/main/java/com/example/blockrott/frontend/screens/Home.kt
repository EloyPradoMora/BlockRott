package com.example.blockrott.frontend.screens

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
import backend.AccesoUsoApps

@Composable
fun HomeScreen(){
    var context = LocalContext.current
    var showStatistics by remember { mutableStateOf(false) }
    var listaEstadisticas by remember { mutableStateOf(emptyList<UsageStats>()) }
    var tiempoTotal by remember { mutableStateOf("0m") }



    fun actualizarEstadisticas(){
        var accesoUso = AccesoUsoApps(context)

        val tiempoTikTok = accesoUso.obtenerTiempoDeUsoAplicacion("com.zhiliaoapp.musically")
        val tiempoYouTube = accesoUso.obtenerTiempoDeUsoAplicacion("com.google.android.youtube")
        val tiempoReddit = accesoUso.obtenerTiempoDeUsoAplicacion("com.reddit.frontpage")
        val tiempoInstagram = accesoUso.obtenerTiempoDeUsoAplicacion("com.instagram.android")


        val listaTemporal = listOf(
            UsageStats("TikTok", accesoUso.convertirTiempoLegible(tiempoTikTok)),
            UsageStats("YouTube", accesoUso.convertirTiempoLegible(tiempoYouTube)),
            UsageStats("Reddit", accesoUso.convertirTiempoLegible(tiempoReddit)),
            UsageStats("Instagram", accesoUso.convertirTiempoLegible(tiempoInstagram))

        )

        val totalTiempo = tiempoTikTok + tiempoYouTube + tiempoReddit

        listaEstadisticas = listaTemporal
        tiempoTotal = accesoUso.convertirTiempoLegible(totalTiempo)
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