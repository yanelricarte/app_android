package ar.edu.gymdemo.ui

import retrofit2.HttpException
import java.io.IOException

/** Traduce excepciones a mensajes para el usuario, sin exponer detalles internos. */
fun Throwable.toUserMessage(): String = when (this) {
    is HttpException -> when (code()) {
        404 -> "No encontrado"
        else -> "Error del servidor (${code()})"
    }
    is IOException -> "Sin conexión con el servidor"
    else -> "Ocurrió un error inesperado"
}
