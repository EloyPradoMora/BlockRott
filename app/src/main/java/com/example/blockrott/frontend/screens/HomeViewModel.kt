package com.example.blockrott.frontend.screens

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import backend.AppMonitorService
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
    val tiempoTotal: String = "0m",
    val permisosConcedidos: Boolean = false
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    private val usuario: Usuario = Usuario.getInstance(application)
    fun verificarPermisos(context: Context) {
        val permisosConcedidos = verificarYPedirPermisosIniciales(context, usuario)
        if (permisosConcedidos) {
            iniciarServicioDeMonitoreo(getApplication())
        }
    }

    fun actualizarEstadisticas(context: Context) {
        if (usuario.especificacionesApp.isEmpty()) {
            //tiempo de un minuto es para demostrar que funciona ELIMINAR AL IMPLEMENTAR UNA FORMA DE CAMBIAR ESTE TIEMPO
            usuario.agregarEspecificacionNueva("YouTube", "com.google.android.youtube", 180000L)
            usuario.agregarEspecificacionNueva("Instagram", "com.instagram.android", 60000L)
        }

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
        usuario.bloquearApps(context)
    }

    fun ocultarEstadisticas() {
        _uiState.update { it.copy(showStatistics = false) }
    }

    private fun iniciarServicioDeMonitoreo(context: Context) {
        val intent = Intent(context, AppMonitorService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, intent)
        } else {
            context.startService(intent)
        }
    }
}