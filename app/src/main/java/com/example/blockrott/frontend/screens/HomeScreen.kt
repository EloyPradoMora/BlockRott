package com.example.blockrott.frontend.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.blockrott.frontend.components.AppStatistics
import com.example.blockrott.frontend.components.StatisticsButton
import com.example.blockrott.frontend.theme.AppTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.blockrott.frontend.components.BlockButton
import com.example.blockrott.frontend.components.SettingsButton
import androidx.compose.runtime.collectAsState

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    // Recolecta el estado del ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Efecto para verificar permisos una sola vez al iniciar
    LaunchedEffect(Unit) {
        viewModel.verificarPermisos(context)
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars),
                horizontalArrangement = Arrangement.End,
            ) {
                SettingsButton(
                    modifier = Modifier.padding(8.dp),
                    onClickAcount = {/* Metodo Acuenta */},
                    onClickNotifications = {/* Metodo Notificaciónes */}
                )
            }
        },
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
                    // Llama a la función del ViewModel
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
            // Lee el estado desde uiState
            if (uiState.showStatistics) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            // Notifica al ViewModel que oculte las estadísticas
                            detectTapGestures { viewModel.ocultarEstadisticas() }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Usa los valores de uiState
                    AppStatistics(uiState.tiempoTotal, uiState.listaEstadisticas)
                }
            } else {
                BlockButton(
                    // Llama a la función del ViewModel
                    onClick = { viewModel.bloquearApps(context) },
                    modifier = Modifier
                        .size(height = 300.dp, width = 300.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    AppTheme {
        HomeScreen()
    }
}
