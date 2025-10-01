package com.example.blockrott.frontend.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.blockrott.frontend.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    // Agregar aqui el codigo
}

@Preview(showBackground = true)
@Composable
fun MyAppPreview() {
    AppTheme {
        MyApp()
    }
}