package com.example.blockrott.frontend.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.blockrott.frontend.theme.ComponentBackground

@Composable
fun StatisticsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    val shapeborder = RoundedCornerShape(50.dp)
    IconButton(
        onClick = onClick,
        modifier = modifier
            .border(
                border = BorderStroke(1.dp, Color.Gray),
                shape = shapeborder
            ),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = ComponentBackground,
        )
    ) {
        /* AGREGAR AQUI LA IMAGEN DE ESTADISTICA */
    }
}