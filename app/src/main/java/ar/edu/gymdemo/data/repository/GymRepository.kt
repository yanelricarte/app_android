package ar.edu.gymdemo.data.repository

import ar.edu.gymdemo.data.dto.AsistenciaCreateRequest
import ar.edu.gymdemo.data.dto.AsistenciaCreatedResponse
import ar.edu.gymdemo.data.dto.AsistenciaListResponse
import ar.edu.gymdemo.data.dto.EstadoSocioResponse
import ar.edu.gymdemo.data.dto.Socio
import ar.edu.gymdemo.data.remote.GymApi
import ar.edu.gymdemo.data.remote.RetrofitClient

/**
 * Capa de datos: envuelve la API y aísla a los ViewModels de Retrofit.
 * Recibe la GymApi por constructor para poder testear con MockWebServer.
 */
class GymRepository(private val api: GymApi = RetrofitClient.api) {

    suspend fun getSocios(): List<Socio> = api.getSocios().items

    suspend fun getEstadoSocio(dni: String): EstadoSocioResponse =
        api.getEstadoSocio(dni)

    suspend fun marcarAsistencia(dni: String): AsistenciaCreatedResponse =
        api.marcarAsistencia(AsistenciaCreateRequest(dni))

    suspend fun listarAsistencias(dni: String, page: Int = 1, size: Int = 10): AsistenciaListResponse =
        api.listarAsistencias(dni = dni, page = page, size = size)
}
