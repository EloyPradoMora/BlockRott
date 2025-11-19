package com.example.blockrott.frontend.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.blockrott.frontend.theme.ComponentBackground
import com.example.blockrott.frontend.theme.ComponentSurface

data class UsageStats(val appName:String, val usageTime: String)

@Composable
fun AppStatistics(
    horasDeUso : String,
    usedApps: List<UsageStats>
){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = ComponentSurface
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .size(width = 350.dp, height = 450.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = horasDeUso,
                    fontSize = 35.sp,
                    modifier = Modifier.padding(top = 30.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        BorderStroke(1.dp, Color.Gray),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.Start
            ){
                Text(
                    text = "Apps mas usadas",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 10.dp,bottom = 5.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ComponentBackground, RoundedCornerShape(10.dp))
                        .padding(1.dp)
                ) {
                    usedApps.forEach { stat ->
                        UsedApps(appName = stat.appName, usageTime = stat.usageTime)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
){
    if (showBottomSheet) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = false
        )
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            content = content,
            containerColor = ComponentBackground
        )
    }
}

@Composable
fun BlockConfig(
    appsList: List<String>,
    timeList: List<Int>,
    onClickConfirm: () -> Unit
){
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Apps", "Min")
    /* VARIABLE DONDE SE GUARDAN LA LISTA DE DE APLICACIÓNES SELECCIÓNADAS */
    val selectedApps = remember {
        mutableListOf<String>()
    }
    /* ESTA VARIABLE ESTABLECE CUANTOS MINUTOS DURARA EL BLOQUEO */
    var selectedMin by remember { mutableIntStateOf(0) }
    Scaffold(
        containerColor = ComponentBackground,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ComponentBackground),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                MySegmentdButton(
                    options = options,
                    selectedIndex = selectedIndex,
                    onOptionSelected = {index -> selectedIndex = index}
                )
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth()
                    .background(ComponentSurface),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (selectedIndex == 0){
                    ConfirmationButton(
                        text = "Confirmar",
                        /*
                            ANTES DE CERRAR VENTANA PRIMERO VALIDAR SI selectedApps NO ESTA VACIO
                            SI LO ESTA GENERAR UN MENSAJE DE [ERROR], SI NO LO ES ->
                            SE CIERRA LA VENTANA Y LLAMA EL PROCESO DE BLOQUEO
                        */
                        onClickConfirm = onClickConfirm
                    )
                } else {
                    ConfirmationButton(
                        // cambiar a 0
                        onClickConfirm = {selectedIndex = 0},
                        text = "Siguiente"
                    )
                }
            }
        }
    ) {
        innerContent ->
        Column(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .fillMaxWidth()
                .padding(innerContent)
                .background(ComponentBackground)
        ) {
            Spacer(modifier = Modifier.padding(8.dp))
            if (selectedIndex == 0) {
                AppConfig(appsList = appsList, selectedApps = selectedApps)
            }
            else {
                TimeConfig(
                    timeList = timeList,
                    currentSelectedMin = selectedMin,
                    onMinSelected = { newMin -> selectedMin = newMin }
                )
            }
        }
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

@Composable
fun AppConfig(
    appsList: List<String>,
    selectedApps: MutableList<String>
){
    appsList.forEach { app ->
        SwitchApp(
            appName = app,
            initialChecked = selectedApps.contains(app),
            selectedApps = selectedApps
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview(){
    /*
    val sampleStats = listOf(
        UsageStats("TikTok", "1h"),
        UsageStats("Youtube", "20 min"),
        UsageStats("Reddit", "6 min")
    )
    AppTheme {
        AppStatistics("1h 26 min", sampleStats)
    }
     */
    val appsPreview = listOf(
        "Instagram","Facebook","TikTok","Youtube","Reddit","Discord"
    )
    val timePreview = listOf(
        10,15,30
    )
    BlockConfig(appsPreview, timePreview,onClickConfirm = {})
}