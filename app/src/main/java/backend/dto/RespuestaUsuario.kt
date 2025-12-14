package backend.dto

data class RespuestaUsuario(
    val id: Long,
    val correo: String,
    val nombreUsuario: String,
    val fechaCreacion: String?,
    val reportes: List<RespuestaReporteSemanal> = emptyList()
)
