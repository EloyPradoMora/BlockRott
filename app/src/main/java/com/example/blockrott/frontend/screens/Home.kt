package com.example.blockrott.frontend.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.blockrott.frontend.components.AppStatistics
import com.example.blockrott.frontend.components.StatisticsButton
import com.example.blockrott.frontend.components.UsageStats
import com.example.blockrott.frontend.theme.AppTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun HomeScreen(){
    var showStatistics by remember { mutableStateOf(false) }
    val sampleStats = listOf(
        UsageStats("TikTok", "1h"),
        UsageStats("Youtube", "20 min"),
        UsageStats("Reddit", "6 min")
    )
    val hoursOfUse = "1h 26 min"
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
                    onClick = {showStatistics = !showStatistics}
                )
            }
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            if (showStatistics){
                AppStatistics(hoursOfUse, sampleStats)
            }
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