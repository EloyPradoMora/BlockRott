package com.example.blockrott.frontend.screens

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import backend.Usuario
import com.example.blockrott.frontend.components.UsageStats
import com.example.blockrott.frontend.utils.calcularTiempoTotal
import com.example.blockrott.frontend.utils.formatearMinutosAHorasMinutos
import com.example.blockrott.frontend.utils.verificarYPedirPermisosIniciales
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estado de la UI
data class HomeUiState(
    val showStatistics: Boolean = false,
    val listaEstadisticas: List<UsageStats> = emptyList(),
    val tiempoTotal: String = "0m"
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Inicializamos Usuario aquí. Usamos el contexto de la aplicación.
    val usuario: Usuario = Usuario().apply {
        val context = application.applicationContext
        agregarEspecificacionNueva("TikTok", "com.zhiliaoapp.musically", 10000, context)
        agregarEspecificacionNueva("YouTube", "com.google.android.youtube", 1200000, context)
        agregarEspecificacionNueva("Reddit", "com.reddit.frontpage", 10000, context)
        agregarEspecificacionNueva("Instagram", "com.instagram.android", 10000, context)
    }

    init {
        // Inicia el monitoreo en segundo plano
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                usuario.monitoreoApps()
                delay(60000L) // 1 minuto
            }
        }
    }

    fun verificarPermisos(context: Context) {
        // Usa el contexto de la Activity para lanzar Intents
        verificarYPedirPermisosIniciales(context, usuario)
    }

    fun actualizarEstadisticas(context: Context) {
        if (!usuario.verificarPermisosUsoEstadistica(context)) {
            verificarYPedirPermisosIniciales(context, usuario)
            return
        }

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

        val totalMinutos = calcularTiempoTotal(listaTemporal)
        val tiempoTotalFormateado = formatearMinutosAHorasMinutos(totalMinutos)

        _uiState.update { currentState ->
            currentState.copy(
                listaEstadisticas = listaTemporal,
                tiempoTotal = tiempoTotalFormateado,
                showStatistics = true
            )
        }
    }

    fun bloquearApps(context: Context) {
        // Pasa el contexto de la Activity para mostrar el diálogo
        usuario.bloquearApps(context)
    }

    fun ocultarEstadisticas() {
        _uiState.update { it.copy(showStatistics = false) }
    }
}