package ar.edu.gymdemo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ar.edu.gymdemo.data.dto.Socio
import ar.edu.gymdemo.data.remote.RetrofitClient
import ar.edu.gymdemo.databinding.FragmentDatosBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DatosFragment : Fragment() {

    private var _b: FragmentDatosBinding? = null
    private val b get() = _b!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentDatosBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b.btnCargar.setOnClickListener { cargarSocios() }
        b.btnBuscarPorDni.setOnClickListener {
            val dni = b.etDni.text.toString().trim()
            if (dni.isEmpty()) Toast.makeText(requireContext(), "Ingresá un DNI", Toast.LENGTH_SHORT).show()
            else consultarEstadoSocio(dni)
        }
    }

    private fun cargarSocios() {
        viewLifecycleOwner.lifecycleScope.launch {
            b.txtResultado.text = "Cargando..."
            try {
                val response = withContext(Dispatchers.IO) { RetrofitClient.api.getSocios() }
                val socios = response.items // tu /clientes devuelve {items:[...]}

                b.txtResultado.text = if (socios.isEmpty()) "No se encontraron socios."
                else socios.joinToString("\n") { s: Socio ->
                    "• ${s.nombre} (Vence: ${s.membresia_vence})"
                }
            } catch (e: Exception) {
                b.txtResultado.text = "Error al cargar: ${e.message}"
            }
        }
    }

    private fun consultarEstadoSocio(dni: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            b.txtResultadoIndividual.text = "Buscando..."
            try {
                val r = withContext(Dispatchers.IO) { RetrofitClient.api.getEstadoSocio(dni) }
                val estado = if (r.activa) "Activa" else "Vencida"
                b.txtResultadoIndividual.text = """
                    Nombre: ${r.nombre}
                    Vence: ${r.vence}
                    Días restantes: ${r.dias_restantes}
                    Estado: $estado
                """.trimIndent()
            } catch (e: Exception) {
                b.txtResultadoIndividual.text = "Error: ${e.message}"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
