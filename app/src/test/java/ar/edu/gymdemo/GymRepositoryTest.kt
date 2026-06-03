package ar.edu.gymdemo

import ar.edu.gymdemo.data.remote.GymApi
import ar.edu.gymdemo.data.repository.GymRepository
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GymRepositoryTest {

    private lateinit var server: MockWebServer
    private lateinit var repo: GymRepository

    @Before
    fun setup() {
        server = MockWebServer()
        server.start()
        val api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GymApi::class.java)
        repo = GymRepository(api)
    }

    @After
    fun teardown() {
        server.shutdown()
    }

    @Test
    fun `getSocios parsea items y mapea membresia_vence a camelCase`() = runTest {
        server.enqueue(
            MockResponse().setResponseCode(200).setBody(
                """{"items":[{"id":1,"nombre":"Ana","membresia_vence":"2025-12-01","activo":true}]}"""
            )
        )

        val socios = repo.getSocios()

        assertEquals(1, socios.size)
        assertEquals("Ana", socios[0].nombre)
        assertEquals("2025-12-01", socios[0].membresiaVence)
        assertTrue(socios[0].activo)
    }

    @Test
    fun `getEstadoSocio mapea dias_restantes a camelCase`() = runTest {
        server.enqueue(
            MockResponse().setResponseCode(200).setBody(
                """{"dni":"123","nombre":"Ana","vence":"2025-12-01","dias_restantes":10,"activa":true}"""
            )
        )

        val estado = repo.getEstadoSocio("123")

        assertEquals(10, estado.diasRestantes)
        assertEquals("Ana", estado.nombre)
        assertTrue(estado.activa)
    }

    @Test
    fun `marcarAsistencia con 402 lanza HttpException con ese codigo`() = runTest {
        server.enqueue(MockResponse().setResponseCode(402).setBody("""{"error":"vencida"}"""))

        try {
            repo.marcarAsistencia("123")
            fail("Debería lanzar HttpException")
        } catch (e: HttpException) {
            assertEquals(402, e.code())
        }
    }

    @Test
    fun `marcarAsistencia con 404 lanza HttpException con ese codigo`() = runTest {
        server.enqueue(MockResponse().setResponseCode(404).setBody("""{"error":"no existe"}"""))

        try {
            repo.marcarAsistencia("123")
            fail("Debería lanzar HttpException")
        } catch (e: HttpException) {
            assertEquals(404, e.code())
        }
    }
}
