package com.example.blockrott.frontend.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.blockrott.frontend.components.StatisticsButton
import com.example.blockrott.frontend.theme.AppTheme

@Composable
fun HomeScreen(){
    Scaffold(
        topBar = {},
        bottomBar = {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                StatisticsButton(
                    modifier = Modifier
                        .height(65.dp)
                        .width(125.dp),
                    onClick = {/* DESPLEGAR VENTANA DE ESTADISTICAS */}
                )
            }
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            /*CONTENIDO CENTRAL*/
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview(){
    AppTheme {
        HomeScreen()
    }
}