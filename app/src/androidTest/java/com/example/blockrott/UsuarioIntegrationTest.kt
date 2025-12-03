package com.example.blockrott

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import backend.Usuario
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UsuarioIntegrationTest {

    @Test
    fun testExtenderTiempoLimite() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val usuario = Usuario.getInstance(context)
        val packageName = "com.test.app.extension"

        usuario.agregarEspecificacionNueva("Test App", packageName, 1000L)
        
        // Verificar si la app existe en las especificaciones
        val specs = usuario.especificacionesApp
        var found = false
        for (spec in specs) {
            if (spec.nombrePaquete == packageName) {
                found = true
                break
            }
        }
        assertTrue("La app de prueba debería haber sido agregada", found)

        // Extender el tiempo maximo de uso
        val extensionTime = 10 * 60 * 1000L
        usuario.extenderTiempoLimite(packageName, extensionTime)

        // Verificar que el tiempo máximo de uso se ha incrementado
        for (spec in specs) {
            if (spec.nombrePaquete == packageName) {
                assertEquals("El tiempo límite debería haberse extendido", 1000L + extensionTime, spec.tiempoMaximoUso)
                break
            }
        }
    }
}
