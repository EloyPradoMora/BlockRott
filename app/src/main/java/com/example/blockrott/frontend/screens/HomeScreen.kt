package com.example.blockrott.frontend.screens

import android.widget.Toast

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.blockrott.frontend.components.AppStatistics
import com.example.blockrott.frontend.components.StatisticsButton
import androidx.compose.runtime.getValue
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.blockrott.frontend.components.BlockButton
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import com.example.blockrott.frontend.components.BlockConfig
import com.example.blockrott.frontend.components.BottomSheet
import com.example.blockrott.frontend.components.TimeLimitConfigSheet
import com.example.blockrott.frontend.theme.*
import com.example.blockrott.frontend.components.TimeConfigItem

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.verificarPermisos(context)
    }
    Box(modifier = Modifier.fillMaxSize().background(brush = backgroundBrush)){
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.navigationBars),
                    contentAlignment = Alignment.Center
                ) {
                    StatisticsButton(
                        modifier = Modifier
                            .padding(8.dp)
                            .height(65.dp)
                            .width(125.dp),
                        onClick = { viewModel.actualizarEstadisticas(context) }
                    )
                }
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                if (uiState.showStatistics) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures {
                                    viewModel.ocultarEstadisticas()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        AppStatistics(
                            useHours = uiState.tiempoTotal,
                            usedApps = uiState.listaEstadisticas,
                            weeklyAverage = uiState.weeklyAverage,
                            dailyHours = uiState.weeklyStats,
                            onConfigClick = { viewModel.mostrarConfiguracionTiempo() }
                        )
                    }
                } else {
                    BlockButton(
                        modifier = Modifier.size(300.dp),
                        onClick = { 
                            if(uiState.isBlockedGlobal){
                                Toast.makeText(context, "Tiempo restante: " + viewModel.obtenerTiempoRestante(), Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.mostrarBlockConfig() 
                            }
                        }
                    )
                }
            }
        }
        if (uiState.showBlockConfig) {
            BottomSheet(
                true,
                {viewModel.ocultarBlockConfig()}
            ){
                BlockConfig(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f),
                    // Entregar estos valores validados
                    appsList = uiState.appsList,
                    timeList = listOf(
                        TimeConfigItem("20 Seg", 20 * 1000L),
                        TimeConfigItem("5 Min", 5 * 60 * 1000L),
                        TimeConfigItem("10 Min", 10 * 60 * 1000L),
                        TimeConfigItem("15 Min", 15 * 60 * 1000L),
                        TimeConfigItem("30 Min", 30 * 60 * 1000L),
                        TimeConfigItem("45 Min", 45 * 60 * 1000L),
                        TimeConfigItem("60 Min", 60 * 60 * 1000L)
                    ),
                    onClickConfirm = { selectedApps, selectedTime ->
                        viewModel.bloquearApps(context, selectedApps, selectedTime)
                    }
                )
            }
        }
        if (uiState.showTimeLimitConfig) {
            TimeLimitConfigSheet(
                appsList = uiState.appsList,
                appLimits = uiState.appLimits,
                onDismiss = { viewModel.ocultarConfiguracionTiempo() },
                onUpdateLimit = { appName, limit ->
                    viewModel.actualizarLimiteApp(appName, limit)
                }
            )
        }
    }
}