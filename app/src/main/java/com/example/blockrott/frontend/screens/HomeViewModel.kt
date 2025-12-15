package com.example.blockrott.frontend.screens

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import backend.AppMonitorService
import backend.Usuario
import backend.api.RetrofitClient
import backend.dto.SolicitudUsoDiario
import android.util.Log
import com.example.blockrott.frontend.components.UsageStats
import com.example.blockrott.frontend.utils.calcularTiempoTotal
import com.example.blockrott.frontend.utils.formatearMinutosAHorasMinutos
import com.example.blockrott.frontend.utils.tiempoAMinutos
import com.example.blockrott.frontend.utils.verificarYPedirPermisosIniciales
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

// Estado de la UI
data class HomeUiState(
    val showStatistics: Boolean = false,
    val listaEstadisticas: List<UsageStats> = emptyList(),
    val tiempoTotal: String = "0m",
    val permisosConcedidos: Boolean = false,
    val showBlockConfig: Boolean = false,
    val appsList: List<String> = emptyList(),
    val showTimeLimitConfig: Boolean = false,
    val appLimits: Map<String, Double> = emptyMap(),
    val weeklyStats: List<Float> = emptyList(),
    val weeklyAverage: String = "0m",
    val isBlockedGlobal: Boolean = false
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    private val usuario: Usuario = Usuario.getInstance(application)
    fun verificarPermisos(context: Context) {
        val permisosConcedidos = verificarYPedirPermisosIniciales(context, usuario)
        if (permisosConcedidos) {
            inicializarApps(getApplication())
            iniciarServicioDeMonitoreo(getApplication())
        }
        verificarEstadoBloqueo()
    }

    private fun inicializarApps(context: Context) {
        // Las apps se cargan en el constructor de usuario
    }
    fun actualizarEstadisticas(context: Context) {
        inicializarApps(context)

        if (!usuario.verificarPermisosUsoEstadistica(context)) {
            verificarYPedirPermisosIniciales(context, usuario)
            return
        }
        val rawUseTimeString = usuario.revisarTiempos()
        val listaTemporal: List<UsageStats> =
                rawUseTimeString
                        .lines()
                        .filter { it.isNotBlank() }
                        .mapNotNull { line ->
                            val parts = line.split(',')
                            if (parts.size == 2) {
                                UsageStats(appName = parts[0].trim(), usageTime = parts[1].trim())
                            } else {
                                null
                            }
                        }
                        .sortedByDescending { tiempoAMinutos(it.usageTime) }

        val totalMinutos = calcularTiempoTotal(listaTemporal)
        val tiempoTotalFormateado = formatearMinutosAHorasMinutos(totalMinutos)

        // Logica para estadisticas semanales (Grafico)
        val hoy = LocalDate.now()
        val inicioSemana = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val dailyTotals = mutableMapOf<String, Long?>()

        // Inicializamos mapa con null
        for (i in 0 until 7) {
            val fecha = inicioSemana.plusDays(i.toLong()).toString()
            dailyTotals[fecha] = null
        }

        // Sumamos el uso de todas las apps monitoreadas
        for (appSpec in usuario.especificacionesApp) {
            val statsApp = usuario.obtenerEstadisticasGrafico(appSpec.nombrePaquete)
            for (i in 0 until 7) {
                val fecha = inicioSemana.plusDays(i.toLong()).toString()
                // En UsoSemanal, si no existe la clave para esa fecha, retorna null (si lo
                // cambiamos a que retorne null)
                // OJO: UsoSemanal retorna Map<String, Long>, el Long puede ser null si asi lo
                // definimos o si usamos getOrDefault
                // En nuestra implementacion java devuelve null si no existe

                if (statsApp.containsKey(fecha)) {
                    val usoDia = statsApp[fecha]
                    if (usoDia != null) {
                        val currentTotal = dailyTotals[fecha] ?: 0L
                        dailyTotals[fecha] = currentTotal + usoDia
                    }
                }
            }
        }

        val weeklyStatsList = mutableListOf<Float>()
        var totalWeeklyMillis = 0L
        var daysContributed = 0

        for (i in 0 until 7) {
            val fecha = inicioSemana.plusDays(i.toLong()).toString()
            val totalDia = dailyTotals[fecha]

            if (totalDia != null) {
                // Convertir a horas para el grafico
                val horas = totalDia.toFloat() / (1000 * 60 * 60)
                weeklyStatsList.add(horas)
                totalWeeklyMillis += totalDia
                daysContributed++
            } else {
                weeklyStatsList.add(0f)
            }
        }

        val avgMillis = if (daysContributed > 0) totalWeeklyMillis / daysContributed else 0
        val avgString = formatearMinutosAHorasMinutos((avgMillis / 1000 / 60).toInt())

        _uiState.update { currentState ->
            currentState.copy(
                    listaEstadisticas = listaTemporal,
                    tiempoTotal = tiempoTotalFormateado,
                    showStatistics = true,
                    weeklyStats = weeklyStatsList,
                    weeklyAverage = avgString
            )
        }

        // Enviar al backend
        viewModelScope.launch(Dispatchers.IO) {
            if (usuario.tieneIdentidad()) {
                val totalSegundos = totalMinutos * 60L
                val solicitud = SolicitudUsoDiario(
                    usuarioCodigo = usuario.getCodigoIdentidad(),
                    tiempoTotalSegundos = totalSegundos
                )
                try {
                    val response = RetrofitClient.api.registrarUsoDiario(solicitud)
                    if (response.isSuccessful) {
                        Log.d("BlockRott", "Tiempo de uso enviado correctamente: $totalSegundos segundos")
                    } else {
                        Log.e("BlockRott", "Error al enviar tiempo de uso: ${response.code()}")
                    }
                } catch (e: Exception) {
                    Log.e("BlockRott", "Excepción al enviar tiempo de uso", e)
                }
            }
        }
    }
    fun mostrarBlockConfig() {
        val currentApps = usuario.especificacionesApp.map { it.nombreApp }
        _uiState.update { it.copy(showBlockConfig = true, appsList = currentApps) }
    }
    fun ocultarBlockConfig() {
        _uiState.update { it.copy(showBlockConfig = false) }
    }

    fun bloquearApps(context: Context, apps: List<String>, durationMillis: Long) {
        usuario.bloquearApps(context, apps, durationMillis)
        ocultarBlockConfig()
        startBlockStatusCheck()
    }
    
    fun verificarEstadoBloqueo(){
        val isBlocked = usuario.isBloqueoGlobal
        _uiState.update { it.copy(isBlockedGlobal = isBlocked) }
        if (isBlocked) {
            startBlockStatusCheck()
        }
    }

    private var checkJob: kotlinx.coroutines.Job? = null

    private fun startBlockStatusCheck() {
        checkJob?.cancel() // Cancel previous if any
        checkJob = viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                val isBlocked = usuario.isBloqueoGlobal
                _uiState.update { it.copy(isBlockedGlobal = isBlocked) }
                if (!isBlocked) {
                    break
                }
                delay(1000)
            }
        }
    }
    
    fun obtenerTiempoRestante(): String {
        return usuario.obtenerTiempoRestanteBloqueo()
    }

    fun ocultarEstadisticas() {
        _uiState.update { it.copy(showStatistics = false) }
    }

    fun mostrarConfiguracionTiempo() {
        val currentApps = usuario.especificacionesApp.map { it.nombreApp }
        val currentLimits =
                usuario.especificacionesApp.associate {
                    val hours = it.tiempoMaximoUso.toDouble() / (1000 * 60 * 60)
                    val displayHours = if (hours > 23.0) 24.0 else (Math.round(hours * 2) / 2.0)
                    it.nombreApp to displayHours
                }
        _uiState.update {
            it.copy(showTimeLimitConfig = true, appsList = currentApps, appLimits = currentLimits)
        }
    }

    fun ocultarConfiguracionTiempo() {
        _uiState.update { it.copy(showTimeLimitConfig = false) }
    }

    fun actualizarLimiteApp(nombreApp: String, horas: Double) {
        // Buscamos el paquete correspondiente al nombre de la app (simplificación)
        val appSpec = usuario.especificacionesApp.find { it.nombreApp == nombreApp }
        appSpec?.let {
            val millis = (horas * 60 * 60 * 1000).toLong()
            usuario.actualizarTiempoLimite(it.nombrePaquete, millis)
        }
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
