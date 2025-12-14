package backend.dto

data class SolicitudReporteSemanal(
    val usuarioCorreo: String,
    val nombreApp: String,
    val promedioUsoSeg: Long
)
