package ar.edu.gymdemo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ar.edu.gymdemo.data.dto.AsistenciaCreateRequest
import ar.edu.gymdemo.data.dto.AsistenciaItem
import ar.edu.gymdemo.data.remote.RetrofitClient
import ar.edu.gymdemo.databinding.FragmentAsistenciaBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AsistenciaFragment : Fragment() {

    private var _b: FragmentAsistenciaBinding? = null
    private val b get() = _b!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentAsistenciaBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b.btnMarcar.setOnClickListener {
            val dni = b.etDniAsistencia.text.toString().trim()
            if (dni.isEmpty()) toast("Ingresá un DNI") else marcarAsistencia(dni)
        }
        b.btnVer.setOnClickListener {
            val dni = b.etDniAsistencia.text.toString().trim()
            if (dni.isEmpty()) toast("Ingresá un DNI") else listarAsistencias(dni)
        }
    }

    private fun marcarAsistencia(dni: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            b.tvEstado.text = "Marcando..."
            try {
                val res = withContext(Dispatchers.IO) {
                    RetrofitClient.api.marcarAsistencia(AsistenciaCreateRequest(dni))
                }
                b.tvEstado.text = "OK: ${res.dni} • ${res.momento.replace('T',' ').take(19)}"
                toast("Asistencia registrada")
            } catch (e: HttpException) {
                b.tvEstado.text = when (e.code()) {
                    402 -> "Membresía vencida (no registrada)"
                    404 -> "Cliente no encontrado"
                    else -> "Error ${e.code()} al marcar"
                }
            } catch (e: Exception) {
                b.tvEstado.text = "Error: ${e.message}"
            }
        }
    }

    private fun listarAsistencias(dni: String, page: Int = 1, size: Int = 10) {
        viewLifecycleOwner.lifecycleScope.launch {
            b.tvLista.text = "Cargando asistencias..."
            try {
                val res = withContext(Dispatchers.IO) {
                    RetrofitClient.api.listarAsistencias(dni = dni, page = page, size = size)
                }
                b.tvLista.text = if (res.items.isEmpty()) "Sin asistencias"
                else render(res.items, res.page, res.total)
            } catch (e: Exception) {
                b.tvLista.text = "Error: ${e.message}"
            }
        }
    }

    private fun render(items: List<AsistenciaItem>, page: Int, total: Int): String {
        val head = "Asistencias (pág. $page) – total: $total"
        val body = items.joinToString("\n") { "• #${it.id} • ${it.dni} • ${it.momento.replace('T',' ').take(19)}" }
        return "$head\n$body"
    }

    private fun toast(msg: String) = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
