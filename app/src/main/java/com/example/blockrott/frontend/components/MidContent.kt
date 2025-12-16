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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.blockrott.frontend.theme.*
import com.example.blockrott.frontend.theme.*
import com.example.blockrott.frontend.utils.FrontendUtils
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem

data class UsageStats(val appName:String, val usageTime: String)
@Composable
fun AppStatistics(
    useHours : String,
    usedApps: List<UsageStats>,
    weeklyAverage: String,
    dailyHours: List<Float>,
    onConfigClick: () -> Unit
){
    val utils = FrontendUtils()
    val week = listOf("Lun","Mar","Mié","Jue","Vie","Sáb","Dom")
    val statistics = listOf("Hoy", "Semanal")
    var selectedIndex by remember { mutableIntStateOf(0) }
    val dataList = remember(usedApps) {
        usedApps.map { data ->
            PieChartEntry(
                percentage = utils.totalMinutes(data.usageTime),
                color = utils.colorApp(data.appName)
            )
        }
    }
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = ComponentSurface),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .size(width = 350.dp, height = 550.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                MySegmentdButton(
                    options = statistics,
                    selectedIndex = 0,
                    onOptionSelected = { index -> selectedIndex = index }
                )
                if (selectedIndex == 0) {
                    Box(
                        modifier = Modifier
                            .size(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        DailyPieChart(
                            data = dataList,
                            modifier = Modifier.matchParentSize()
                        )
                        Text(
                            text = useHours,
                            fontSize = 35.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    AppStatisticsContent(usedApps = usedApps)
                } else {
                    WeekStatistics(dailyHours, week, weeklyAverage)
                }
            }
            // Icono de engranaje
            IconButton(
                onClick = onConfigClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Configurar Tiempo",
                    tint = Color.Gray
                )
            }
        }
    }
}
@Composable
fun WeekStatistics(
    values: List<Float>,
    labels: List<String>,
    promSemanal: String
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(12.dp)
            .testTag("WeekStatsContainer")
    ) {
        Text(
            text = "Promedio     \n     Semanal",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag("Prom Semanal")
        )
        Spacer(Modifier.height(16.dp))

        WeeklyBarChart(values, labels)

        Spacer(Modifier.height(16.dp))
        Text(
            text = promSemanal,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
fun AppStatisticsContent(usedApps: List<UsageStats>){
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
            modifier = Modifier
                .padding(start = 10.dp,bottom = 5.dp)
                .testTag("AppContentTitle")
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(ComponentBackground, RoundedCornerShape(10.dp))
                .padding(1.dp)
        ) {
            val utils = FrontendUtils()
            usedApps.take(3).forEach { stat ->
                UsedApps(
                    appName = stat.appName, 
                    usageTime = stat.usageTime,
                    color = utils.colorApp(stat.appName)
                )
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
fun AppConfig(
    appsList: List<String>,
    selectedApps: SnapshotStateList<String>
){
    val onToggleApp: (String) -> Unit = remember(selectedApps) {
        { app ->
            if (selectedApps.contains(app)) {
                selectedApps.remove(app)
            } else {
                selectedApps.add(app)
            }
        }
    }
    Column {
        appsList.forEach { app ->
            key(app) {
                SwitchApp(
                    appName = app,
                    checked = selectedApps.contains(app),
                    onToggle = onToggleApp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeLimitConfigSheet(
    appsList: List<String>,
    appLimits: Map<String, Double>,
    onDismiss: () -> Unit,
    onUpdateLimit: (String, Double) -> Unit
) {
    val options = listOf(0.5, 1.0, 1.5, 2.0, 2.5, 24.0)
    BottomSheet(showBottomSheet = true, onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Configurar Límite de Tiempo",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            appsList.forEach { appName ->
                Row(
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(vertical = 8.dp),
                   verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = appName, fontSize = 18.sp, modifier = Modifier.weight(1f))
                    var expanded by remember { mutableStateOf(false) }
                    // Inicializar con limite actual o 24 hrs si no se detecta
                    var selectedOption by remember { mutableStateOf(appLimits[appName] ?: 24.0) }
                    
                    Box {
                        Button(onClick = { expanded = true }) {
                            Text(text = if(selectedOption >= 24.0) "Sin Límite" else "${selectedOption}h")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            options.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(if(option == 24.0) "24h (Sin Límite)" else "${option}h") },
                                    onClick = { 
                                        selectedOption = option
                                        expanded = false
                                        onUpdateLimit(appName, option)
                                    }
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview(){

}