package com.example.blockrott.frontend.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import backend.Usuario
import backend.api.RetrofitClient
import com.example.blockrott.frontend.screens.HomeScreen
import com.example.blockrott.frontend.screens.TermsAndConditionsScreen
import com.example.blockrott.frontend.theme.AppTheme
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Registro Silencioso
        val usuario = backend.Usuario.getInstance(this)
        if (!usuario.tieneIdentidad()) {
            lifecycleScope.launch {
                try {
                    val modeloDispositivo =
                            "${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}"
                    val response =
                            backend.api.RetrofitClient.api.registrarDispositivo(modeloDispositivo)
                    if (response.isSuccessful) {
                        val uuid = response.body()?.string()
                        if (uuid != null) {
                            usuario.guardarIdentidad(uuid)
                            android.util.Log.d("SilentReg", "Registered: $uuid")
                        }
                    } else {
                        android.util.Log.e("SilentReg", "Failed: ${response.code()}")
                    }
                } catch (e: Exception) {
                    android.util.Log.e("SilentReg", "Error", e)
                }
            }
        }

        MobileAds.initialize(this) {}
        setContent { AppTheme { MyApp() } }
    }
}

@Composable
fun MyApp() {
    val context = LocalContext.current
    val usuario = remember { backend.Usuario.getInstance(context) }
    var showTerms by remember { mutableStateOf(!usuario.isTermsAccepted) }

    if (showTerms) {
        TermsAndConditionsScreen(onTermsAccepted = { showTerms = false })
    } else {
        HomeScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun MyAppPreview() {
    AppTheme { MyApp() }
}
