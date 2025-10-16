package ar.edu.gymdemo.data.dto

data class AsistenciaCreateRequest(val dni: String)

data class AsistenciaCreatedResponse(
    val ok: Boolean,
    val dni: String,
    val momento: String
)

data class AsistenciaItem(
    val id: Int,
    val dni: String,
    val momento: String
)

data class AsistenciaListResponse(
    val page: Int,
    val size: Int,
    val total: Int,
    val items: List<AsistenciaItem>
)
