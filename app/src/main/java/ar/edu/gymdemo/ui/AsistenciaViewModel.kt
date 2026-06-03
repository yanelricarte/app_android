package ar.edu.gymdemo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.gymdemo.data.dto.AsistenciaCreatedResponse
import ar.edu.gymdemo.data.dto.AsistenciaListResponse
import ar.edu.gymdemo.data.repository.GymRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AsistenciaViewModel(private val repo: GymRepository = GymRepository()) : ViewModel() {

    private val _marcar = MutableStateFlow<UiState<AsistenciaCreatedResponse>>(UiState.Idle)
    val marcar: StateFlow<UiState<AsistenciaCreatedResponse>> = _marcar.asStateFlow()

    private val _lista = MutableStateFlow<UiState<AsistenciaListResponse>>(UiState.Idle)
    val lista: StateFlow<UiState<AsistenciaListResponse>> = _lista.asStateFlow()

    fun marcarAsistencia(dni: String) {
        _marcar.value = UiState.Loading
        viewModelScope.launch {
            _marcar.value = runCatching { repo.marcarAsistencia(dni) }
                .fold({ UiState.Success(it) }, { UiState.Error(marcarError(it)) })
        }
    }

    fun listarAsistencias(dni: String, page: Int = 1, size: Int = 10) {
        _lista.value = UiState.Loading
        viewModelScope.launch {
            _lista.value = runCatching { repo.listarAsistencias(dni, page, size) }
                .fold({ UiState.Success(it) }, { UiState.Error(it.toUserMessage()) })
        }
    }

    private fun marcarError(t: Throwable): String = when (t) {
        is HttpException -> when (t.code()) {
            402 -> "Membresía vencida (no registrada)"
            404 -> "Cliente no encontrado"
            else -> "Error ${t.code()} al marcar"
        }
        else -> t.toUserMessage()
    }
}
