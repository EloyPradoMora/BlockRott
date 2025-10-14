package com.example.blockrott.frontend.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.blockrott.frontend.theme.ComponentBackground
import androidx.compose.runtime.*

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

@Composable
fun BlockButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Surface(
        modifier = modifier,
        shape = CircleShape,
        border = BorderStroke(5.dp, Color.Gray),
        color = ComponentBackground,
        onClick = onClick
    ) {
        /* IMAGEN BOTON DE CLOCKEO */
    }
}

@Composable
fun SettingsButton(
    modifier: Modifier = Modifier,
    onClickAcount: () -> Unit,
    onClickNotifications: () -> Unit
){
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        IconButton(
            onClick = {expanded = !expanded},
            modifier = Modifier.size(65.dp)
            ) {
            Icon(
                Icons.Default.Menu,
                contentDescription = "Menu",
                modifier = Modifier.size(65.dp)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {expanded = false}
        ) {
            DropdownMenuItem(
                text = { Text("Cuenta") },
                onClick = onClickAcount
            )
            DropdownMenuItem(
                text = { Text("Notificaciones") },
                onClick = onClickNotifications
            )
        }
    }
}