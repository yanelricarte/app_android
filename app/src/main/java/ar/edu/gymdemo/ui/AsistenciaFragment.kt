package ar.edu.gymdemo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import ar.edu.gymdemo.data.dto.AsistenciaItem
import ar.edu.gymdemo.databinding.FragmentAsistenciaBinding
import kotlinx.coroutines.launch

class AsistenciaFragment : Fragment() {

    private var _b: FragmentAsistenciaBinding? = null
    private val b get() = _b!!
    private val vm: AsistenciaViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentAsistenciaBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b.btnMarcar.setOnClickListener {
            val dni = b.etDniAsistencia.text.toString().trim()
            if (dni.isEmpty()) toast("Ingresá un DNI") else vm.marcarAsistencia(dni)
        }
        b.btnVer.setOnClickListener {
            val dni = b.etDniAsistencia.text.toString().trim()
            if (dni.isEmpty()) toast("Ingresá un DNI") else vm.listarAsistencias(dni)
        }
        observarEstados()
    }

    private fun observarEstados() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.marcar.collect { state ->
                        when (state) {
                            is UiState.Idle -> Unit
                            is UiState.Loading -> b.tvEstado.text = "Marcando..."
                            is UiState.Success -> b.tvEstado.text =
                                "OK: ${state.data.dni} • ${state.data.momento.replace('T', ' ').take(19)}"
                            is UiState.Error -> b.tvEstado.text = state.message
                        }
                    }
                }
                launch {
                    vm.lista.collect { state ->
                        when (state) {
                            is UiState.Idle -> Unit
                            is UiState.Loading -> b.tvLista.text = "Cargando asistencias..."
                            is UiState.Success -> b.tvLista.text =
                                if (state.data.items.isEmpty()) "Sin asistencias"
                                else render(state.data.items, state.data.page, state.data.total)
                            is UiState.Error -> b.tvLista.text = state.message
                        }
                    }
                }
            }
        }
    }

    private fun render(items: List<AsistenciaItem>, page: Int, total: Int): String {
        val head = "Asistencias (pág. $page) – total: $total"
        val body = items.joinToString("\n") {
            "• #${it.id} • ${it.dni} • ${it.momento.replace('T', ' ').take(19)}"
        }
        return "$head\n$body"
    }

    private fun toast(msg: String) = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
