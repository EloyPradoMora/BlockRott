package com.example.blockrott.frontend.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.blockrott.frontend.screens.HomeScreen
import com.example.blockrott.frontend.theme.AppTheme
import com.google.android.gms.ads.MobileAds
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.util.Log
import backend.Usuario
import backend.api.RetrofitClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Registro Silencioso
        val usuario = backend.Usuario.getInstance(this)
        if (!usuario.tieneIdentidad()) {
            lifecycleScope.launch {
                try {
                    val response = backend.api.RetrofitClient.api.registrarDispositivo()
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
        setContent {
            AppTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    HomeScreen()
}

@Preview(showBackground = true)
@Composable
fun MyAppPreview() {
    AppTheme {
        MyApp()
    }
}