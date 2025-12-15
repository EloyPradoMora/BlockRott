package backend.api
import backend.dto.RespuestaUsuario
import backend.dto.SolicitudRegistro
import backend.dto.SolicitudReporteSemanal
import retrofit2.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BlockRottApiService {
    // CAMBIO AQUI: De Response<String> a Response<ResponseBody>
    @POST("api/usuarios/registro")
    suspend fun registrarUsuario(
        @Body request: SolicitudRegistro
    ): Response<ResponseBody>

    // Los demas se quedan igual porque SÍ devuelven JSON
    @GET("api/usuarios/buscar")
    suspend fun buscarUsuarioPorCorreo(
        @Query("correo") correo: String
    ): Response<RespuestaUsuario>

    // CAMBIO AQUI TAMBIEN (El reporte tambien devuelve texto plano "Reporte guardado")
    @POST("api/reportes")
    suspend fun enviarReporte(
        @Body request: SolicitudReporteSemanal
    ): Response<ResponseBody>

    @POST("api/usuarios/anonimo")
    suspend fun registrarDispositivo(): Response<ResponseBody>
}
