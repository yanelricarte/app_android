package ar.edu.gymdemo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.gymdemo.data.dto.EstadoSocioResponse
import ar.edu.gymdemo.data.dto.Socio
import ar.edu.gymdemo.data.repository.GymRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DatosViewModel(private val repo: GymRepository = GymRepository()) : ViewModel() {

    private val _socios = MutableStateFlow<UiState<List<Socio>>>(UiState.Idle)
    val socios: StateFlow<UiState<List<Socio>>> = _socios.asStateFlow()

    private val _estado = MutableStateFlow<UiState<EstadoSocioResponse>>(UiState.Idle)
    val estado: StateFlow<UiState<EstadoSocioResponse>> = _estado.asStateFlow()

    fun cargarSocios() {
        _socios.value = UiState.Loading
        viewModelScope.launch {
            _socios.value = runCatching { repo.getSocios() }
                .fold({ UiState.Success(it) }, { UiState.Error(it.toUserMessage()) })
        }
    }

    fun consultarEstado(dni: String) {
        _estado.value = UiState.Loading
        viewModelScope.launch {
            _estado.value = runCatching { repo.getEstadoSocio(dni) }
                .fold({ UiState.Success(it) }, { UiState.Error(it.toUserMessage()) })
        }
    }
}
