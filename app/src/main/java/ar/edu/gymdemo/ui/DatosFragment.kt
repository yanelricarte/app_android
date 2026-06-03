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
import ar.edu.gymdemo.data.dto.Socio
import ar.edu.gymdemo.databinding.FragmentDatosBinding
import kotlinx.coroutines.launch

class DatosFragment : Fragment() {

    private var _b: FragmentDatosBinding? = null
    private val b get() = _b!!
    private val vm: DatosViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentDatosBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b.btnCargar.setOnClickListener { vm.cargarSocios() }
        b.btnBuscarPorDni.setOnClickListener {
            val dni = b.etDni.text.toString().trim()
            if (dni.isEmpty()) toast("Ingresá un DNI") else vm.consultarEstado(dni)
        }
        observarEstados()
    }

    private fun observarEstados() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.socios.collect { state ->
                        when (state) {
                            is UiState.Idle -> Unit
                            is UiState.Loading -> b.txtResultado.text = "Cargando..."
                            is UiState.Success -> b.txtResultado.text =
                                if (state.data.isEmpty()) "No se encontraron socios."
                                else state.data.joinToString("\n") { s: Socio ->
                                    "• ${s.nombre} (Vence: ${s.membresiaVence})"
                                }
                            is UiState.Error -> b.txtResultado.text = state.message
                        }
                    }
                }
                launch {
                    vm.estado.collect { state ->
                        when (state) {
                            is UiState.Idle -> Unit
                            is UiState.Loading -> b.txtResultadoIndividual.text = "Buscando..."
                            is UiState.Success -> {
                                val r = state.data
                                b.txtResultadoIndividual.text = """
                                    Nombre: ${r.nombre}
                                    Vence: ${r.vence}
                                    Días restantes: ${r.diasRestantes}
                                    Estado: ${if (r.activa) "Activa" else "Vencida"}
                                """.trimIndent()
                            }
                            is UiState.Error -> b.txtResultadoIndividual.text = state.message
                        }
                    }
                }
            }
        }
    }

    private fun toast(msg: String) = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
