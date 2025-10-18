package mx.tec.proyectoBJ.viewmodel

import io.mockk.coEvery
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mx.tec.proyectoBJ.model.ServicioRemoto
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [24])
@OptIn(ExperimentalCoroutinesApi::class)
class AppVMTest {

    // Rule to swap the main dispatcher for a test dispatcher
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: AppVM

    @Before
    fun setUp() {
        // Sets the main dispatcher to our test dispatcher
        Dispatchers.setMain(testDispatcher)
        // Mock the ServicioRemoto object
        mockkObject(ServicioRemoto)
        // Instantiate the ViewModel
        viewModel = AppVM()
    }

    @After
    fun tearDown() {
        // Resets the main dispatcher
        Dispatchers.resetMain()
    }

    @Test
    fun `enviarUsuario debe mandar estado Success en caso de datos válidos`() = runTest {
        // 1. Arrange
        // Make the mocked service return a success result
        coEvery { ServicioRemoto.registrarUsuario(any()) } returns Result.success(Unit)

        // 2. Act
        viewModel.enviarUsuario(
            nombre = "Yooku",
            apellidos = "Masta Limima",
            correo = "ejemplo@gmail.com",
            contrasena = "ATS_BrrT5",
            direccion = "Av. Juárez 54",
            numeroTelefono = "5501349825",
            curp = "MALY131015HASSMKA0"
        )

        // Advance the dispatcher to allow the coroutine to execute
        testDispatcher.scheduler.advanceUntilIdle()

        // 3. Assert
        // Check if the state was updated to Success
        val finalState = viewModel.estatusRegistro.value
        assertTrue("Registro exitoso", finalState is EstadoRegistroUI.Success)
    }

    @Test
    fun `enviarUsuario debe mandar el estado Error si el correo no tiene un arroba`() = runTest {
        coEvery { ServicioRemoto.registrarUsuario(any()) } returns Result.success(Unit)

        viewModel.enviarUsuario(
            nombre = "Yooku",
            apellidos = "Masta Limima",
            correo = "ejemploemail.com",
            contrasena = "AL-32",
            direccion = "Av. Juárez 54",
            numeroTelefono = "5501349825",
            curp = "MALY131015HASSMKA0"
        )

        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.estatusRegistro.value
        assertTrue("Error: El correo no es válido", finalState is EstadoRegistroUI.Error)

    }

    @Test
    fun `enviarUsuario debe mandar el estado Error si el correo tiene doble arroba`() = runTest {
        coEvery { ServicioRemoto.registrarUsuario(any()) } returns Result.success(Unit)

        viewModel.enviarUsuario(
            nombre = "Yooku",
            apellidos = "Masta Limima",
            correo = "ejemplo@@email.com",
            contrasena = "AL-32",
            direccion = "Av. Juárez 54",
            numeroTelefono = "5501349825",
            curp = "MALY131015HASSMKA0"
        )

        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.estatusRegistro.value
        assertTrue("Error: El correo no es válido", finalState is EstadoRegistroUI.Error)
    }

    @Test
    fun `enviarUsuario debe mandar el estado Error si el correo tiene TLD de una letra`() = runTest {
        coEvery { ServicioRemoto.registrarUsuario(any()) } returns Result.success(Unit)

        viewModel.enviarUsuario(
            nombre = "Yooku",
            apellidos = "Masta Limima",
            correo = "ejemplo@email.c",
            contrasena = "AL-32",
            direccion = "Av. Juárez 54",
            numeroTelefono = "5501349825",
            curp = "MALY131015HASSMKA0"
        )

        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.estatusRegistro.value
        assertTrue("Error: El correo no es válido", finalState is EstadoRegistroUI.Error)
    }

    @Test
    fun `enviarUsuario debe mandar el estado Error si el correo tiene espacios`() = runTest {
        coEvery { ServicioRemoto.registrarUsuario(any()) } returns Result.success(Unit)

        viewModel.enviarUsuario(
            nombre = "Yooku",
            apellidos = "Masta Limima",
            correo = "el ejemplo@gmail.com",
            contrasena = "AL-32",
            direccion = "Av. Juárez 54",
            numeroTelefono = "5501349825",
            curp = "MALY131015HASSMKA0"
        )

        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.estatusRegistro.value
        assertTrue("Error: El correo no es válido", finalState is EstadoRegistroUI.Error)
    }

    @Test
    fun `enviarUsuario debe mandar el estado Error si la contrasena es muy corta`() = runTest {
        coEvery { ServicioRemoto.registrarUsuario(any()) } returns Result.success(Unit)

        viewModel.enviarUsuario(
            nombre = "Yooku",
            apellidos = "Masta Limima",
            correo = "ejemplo@email.com",
            contrasena = "AL-32",
            direccion = "Av. Juárez 54",
            numeroTelefono = "5501349825",
            curp = "MALY131015HASSMKA0"
        )

        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.estatusRegistro.value
        assertTrue("Error: La contraseña no es válida", finalState is EstadoRegistroUI.Error)

    }

    @Test
    fun `enviarUsuario debe mandar el estado Error si la contraseña no tiene símbolos`() = runTest {
        coEvery { ServicioRemoto.registrarUsuario(any()) } returns Result.success(Unit)

        viewModel.enviarUsuario(
            nombre = "Yooku",
            apellidos = "Masta Limima",
            correo = "ejemplo@email.com",
            contrasena = "Dedalus32",
            direccion = "Av. Juárez 54",
            numeroTelefono = "5501349825",
            curp = "MALY131015HASSMKA0"
        )

        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.estatusRegistro.value
        assertTrue("Error: La contraseña no es válida", finalState is EstadoRegistroUI.Error)

    }

    @Test
    fun `enviarUsuario debe mandar el estado Error si la contraseña no tiene números`() = runTest {
        coEvery { ServicioRemoto.registrarUsuario(any()) } returns Result.success(Unit)

        viewModel.enviarUsuario(
            nombre = "Yooku",
            apellidos = "Masta Limima",
            correo = "ejemplo@email.com",
            contrasena = "Sauron_black",
            direccion = "Av. Juárez 54",
            numeroTelefono = "5501349825",
            curp = "MALY131015HASSMKA0"
        )

        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.estatusRegistro.value
        assertTrue("Error: La contraseña no es válida", finalState is EstadoRegistroUI.Error)

    }

}
