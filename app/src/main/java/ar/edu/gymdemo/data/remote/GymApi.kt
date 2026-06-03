package ar.edu.gymdemo.data.remote

import ar.edu.gymdemo.data.dto.AsistenciaCreateRequest
import ar.edu.gymdemo.data.dto.AsistenciaCreatedResponse
import ar.edu.gymdemo.data.dto.AsistenciaListResponse
import ar.edu.gymdemo.data.dto.ClientesResponse
import ar.edu.gymdemo.data.dto.EstadoSocioResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GymApi {
    @GET("clientes")
    suspend fun getSocios(): ClientesResponse

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
        @Query("hasta") hasta: String? = null,
    ): AsistenciaListResponse
}
