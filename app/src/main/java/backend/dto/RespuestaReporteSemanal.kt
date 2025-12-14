package backend.dto

data class RespuestaReporteSemanal(
    val id: Long,
    val nombreApp: String,
    val semanaInicio: String?, // Puede venir nulo
    val promedioUsoSeg: Long,
    val usuarioId: Long
)
