package backend.dto

data class SolicitudUsoDiario(
    val usuarioCodigo: String,       // El UUID del dispositivo
    val tiempoTotalSegundos: Long    // El tiempo calculado
)
