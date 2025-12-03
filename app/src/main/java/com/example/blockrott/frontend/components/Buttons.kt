package com.example.blockrott.frontend.components

import androidx.compose.material3.Switch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.sp
import com.example.blockrott.frontend.theme.SecondaryColor
import com.example.blockrott.frontend.theme.TextSecondary
import com.example.blockrott.frontend.theme.TimerTextSelected
import com.example.blockrott.frontend.theme.TimerTextUnselected

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

@Composable
fun SwitchBlock(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySegmentdButton(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit
){
    Surface(
        color = TimerTextUnselected,
        shape = RoundedCornerShape(25.dp),
        modifier = Modifier.fillMaxWidth(0.7f)
    ) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.padding(4.dp)
        ) {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = RoundedCornerShape(20.dp),
                    onClick = { onOptionSelected(index)},
                    selected = index == selectedIndex,
                    colors = SegmentedButtonDefaults.colors(
                        activeContentColor = TimerTextSelected,
                        inactiveContentColor = TimerTextSelected,
                        activeContainerColor = TextSecondary,
                        inactiveContainerColor = TimerTextUnselected
                    ),
                    label = {
                        Text(
                            label,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    },
                    icon = {}
                )
            }
        }
    }
}

@Composable
fun ConfirmationButton(
    onClickConfirm: () -> Unit,
    text: String
){
    Button(
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth(0.5f)
            .padding(8.dp),
        onClick = onClickConfirm,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = SecondaryColor
        )
    ) {
        Text(
            text = text,
            fontSize = 22.sp,
        )
    }
}