package ar.edu.gymdemo.data.dto

import com.google.gson.annotations.SerializedName

data class Socio(
    val id: Int,
    val nombre: String,
    @SerializedName("membresia_vence") val membresiaVence: String,
    val activo: Boolean,
)

// Respuesta de /clientes: { "items": [ ... ] }
data class ClientesResponse(val items: List<Socio>)

// Respuesta de /clientes/estado?dni=...
data class EstadoSocioResponse(
    val dni: String,
    val nombre: String,
    val vence: String,
    @SerializedName("dias_restantes") val diasRestantes: Int,
    val activa: Boolean,
)
