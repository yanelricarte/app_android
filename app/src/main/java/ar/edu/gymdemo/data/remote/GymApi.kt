package ar.edu.gymdemo.data.remote

import ar.edu.gymdemo.data.dto.Socio
import ar.edu.gymdemo.data.dto.AsistenciaCreateRequest
import ar.edu.gymdemo.data.dto.AsistenciaCreatedResponse
import ar.edu.gymdemo.data.dto.AsistenciaListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// Respuesta de /clientes tal como devuelve tu API: { "items": [ ... ] }
data class ClientesResponse(val items: List<Socio>)

// Respuesta de /clientes/estado?dni=...
data class EstadoSocioResponse(
    val dni: String,
    val nombre: String,
    val vence: String,
    val dias_restantes: Int,
    val activa: Boolean
)

interface GymApi {
    @GET("clientes")
    suspend fun getSocios(): ClientesResponse
    // Si algún día /clientes devuelve ARRAY plano, cambia a: suspend fun getSocios(): List<Socio>

    @GET("clientes/estado")
    suspend fun getEstadoSocio(@Query("dni") dni: String): EstadoSocioResponse

    @POST("asistencia")
    suspend fun marcarAsistencia(@Body body: AsistenciaCreateRequest): AsistenciaCreatedResponse

    @GET("asistencias")
    suspend fun listarAsistencias(
        @Query("dni") dni: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20,
        @Query("desde") desde: String? = null,
        @Query("hasta") hasta: String? = null
    ): AsistenciaListResponse
}
