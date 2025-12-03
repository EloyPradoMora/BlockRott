package com.example.blockrott

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import backend.BlockerActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BlockerActivityIntegrationTest {

    @Test
    fun testGlobalLockUI() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), BlockerActivity::class.java).apply {
            putExtra("PACKAGE_NAME", "com.test.app")
            putExtra("BLOCK_REASON", "GLOBAL_LOCK")
        }

        ActivityScenario.launch<BlockerActivity>(intent).use {
            onView(withId(R.id.blocked_app_name)).check(matches(withText("com.test.app")))
            onView(withId(R.id.blocked_message_text)).check(matches(withText("Aplicación bloqueada por el control de tiempo global.")))
            onView(withId(R.id.close_blocker_button)).check(matches(withText("Entendido")))
        }
    }

    @Test
    fun testTimeLimitLockUI() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), BlockerActivity::class.java).apply {
            putExtra("PACKAGE_NAME", "com.test.app")
            putExtra("BLOCK_REASON", "TIME_LIMIT")
        }

        ActivityScenario.launch<BlockerActivity>(intent).use {
            onView(withId(R.id.blocked_app_name)).check(matches(withText("com.test.app")))
            onView(withId(R.id.blocked_message_text)).check(matches(withText("¡Límite de tiempo alcanzado! ¿Quieres una extensión?")))
            onView(withId(R.id.close_blocker_button)).check(matches(withText("Ver anuncio para usar por 10 minutos mas")))
        }
    }

    @Test
    fun testAdRewardFunctionality() {
        val packageName = "com.test.app.reward"
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val usuario = backend.Usuario.getInstance(context)
        
        // Agregar app de prueba
        usuario.agregarEspecificacionNueva("Reward App", packageName, 1000L)
        
        val intent = Intent(context, BlockerActivity::class.java).apply {
            putExtra("PACKAGE_NAME", packageName)
            putExtra("BLOCK_REASON", "TIME_LIMIT")
        }

        ActivityScenario.launch<BlockerActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                // Simular que el usuario vio el anuncio y obtuvo la recompensa
                activity.handleAdReward(packageName)
            }
        }

        // Verificar que el tiempo se extendió
        val specs = usuario.especificacionesApp
        for (spec in specs) {
            if (spec.nombrePaquete == packageName) {
                val expectedTime = 1000L + (10 * 60 * 1000L)
                assert(spec.tiempoMaximoUso == expectedTime) { "El tiempo no se extendió correctamente" }
                break
            }
        }
    }
}
