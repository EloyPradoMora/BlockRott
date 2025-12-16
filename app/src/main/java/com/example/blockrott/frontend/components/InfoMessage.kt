package com.example.blockrott.frontend.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.blockrott.frontend.theme.*

import androidx.compose.ui.window.DialogProperties

enum class DialogType {
    ERROR,
    SUCCESS,
    WARNING,
    INFO
}
@Composable
fun AlertDialog(
    type: DialogType,
    onConfirmation: () -> Unit,
    onDismiss: () -> Unit,
    dialogTitle: String,
    dialogExplanation: String,
    confirmText: String = "Entendido",
    dismissText: String = "Cerrar",
    properties: DialogProperties = DialogProperties()
) {
    val icon: ImageVector
    val iconTint: Color

    when (type) {
        DialogType.ERROR -> {
            icon = Icons.Default.Close
            iconTint = Error
        }
        DialogType.SUCCESS -> {
            icon = Icons.Default.CheckCircle
            iconTint = Success
        }
        DialogType.WARNING -> {
            icon = Icons.Default.Warning
            iconTint = Warning
        }
        DialogType.INFO -> {
            icon = Icons.Default.Info
            iconTint = Information
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = properties,
        icon = {
            Icon(icon, contentDescription = type.name, tint = iconTint)
        },
        title = {
            Text(
                text = dialogTitle,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = iconTint
            )
        },
        text = {
            Text(
                text = dialogExplanation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmation) {
                Text(confirmText, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.secondary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewDialog(){
    val tiposTitulos = listOf("Operacion Fallida","Operación Exitosa","Advertencia","Atención")
    MaterialTheme {
        AlertDialog(
            type = DialogType.INFO,
            dialogTitle = tiposTitulos[3],
            dialogExplanation = "Mensaje de prueba generado para testear el maximo de información que puede aguantar el texto",
            onConfirmation = { },
            onDismiss = { }
        )
    }
}