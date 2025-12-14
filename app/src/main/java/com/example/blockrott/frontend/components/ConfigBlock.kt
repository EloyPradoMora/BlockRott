package com.example.blockrott.frontend.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.blockrott.frontend.theme.ComponentBackground
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.blockrott.frontend.theme.ComponentSurface

@Composable
fun BlockConfig(
    modifier: Modifier = Modifier,
    appsList: List<String>,
    timeList: List<Int>,
    onClickConfirm: () -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Apps", "Min")
    var showWarningDialog by remember { mutableStateOf(false) }
    val selectedApps = remember { mutableStateListOf<String>() }
    var selectedMin by remember { mutableIntStateOf(0) }
    if (showWarningDialog) {
        AlertDialog(
            type = DialogType.WARNING,
            onDismiss = { showWarningDialog = false },
            onConfirmation = { showWarningDialog = false },
            dialogTitle = "ADVERTENCIA",
            dialogExplanation = "No puede continuar si no a selecciónado ninguna aplicación"
        )
    }
    Column(
        modifier = modifier
            .background(ComponentBackground)
            .padding(16.dp)
    ) {

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            MySegmentdButton(
                options = options,
                selectedIndex = selectedIndex,
                onOptionSelected = { selectedIndex = it }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            if (selectedIndex == 0) {
                items(appsList, key = { it }) { app ->
                    AppSelector(selectedApps,app)
                }
            }
            else {
                items(timeList, key = { it }) { time ->
                    SwitchTime(
                        timeConfig = time,
                        initialChecked = time == selectedMin,
                        onMinSelected = { selectedMin = it }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            if (selectedIndex == 0) {
                ConfirmationButton(
                    text = "Confirmar",
                    onClickConfirm = {
                        if (selectedApps.isEmpty()){
                            showWarningDialog = true
                        } else{ onClickConfirm() }
                    }
                )
            } else {
                ConfirmationButton(
                    text = "Siguiente",
                    onClickConfirm = { selectedIndex = 0 }
                )
            }
        }
    }
}

@Composable
fun AppSelector(
    selectedApps: MutableList<String>,
    app: String
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ComponentSurface)
            .border(
                BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = app,
            fontSize = 22.sp,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = selectedApps.contains(app),
            onCheckedChange = { isChecked ->
                if (isChecked) {
                    if (!selectedApps.contains(app)) {
                        selectedApps.add(app)
                    }
                } else {
                    selectedApps.remove(app)
                }
            }
        )
    }
}
@Composable
fun TimeConfig(
    timeList: List<Int>,
    currentSelectedMin: Int,
    onMinSelected: (Int) -> Unit
){
    timeList.forEach { time ->
        SwitchTime(
            timeConfig = time,
            initialChecked = time == currentSelectedMin,
            onMinSelected = onMinSelected
        )
    }
}