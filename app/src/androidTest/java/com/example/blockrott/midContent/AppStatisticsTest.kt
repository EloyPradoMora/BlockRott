package com.example.blockrott.midContent

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.blockrott.frontend.components.AppStatistics
import com.example.blockrott.frontend.components.AppStatisticsContent
import com.example.blockrott.frontend.components.UsageStats
import com.example.blockrott.frontend.components.WeekStatistics
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppStatisticsTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val testUsedApps = listOf(
        UsageStats(appName = "Facebook", usageTime = "1h 30min"),
        UsageStats(appName = "Instagram", usageTime = "45m")
    )
    private val testDailyHours = listOf(10f, 15f, 20f, 5f, 12f, 8f, 18f)
    private val testWeekLabels = listOf("Lun","Mar","Mié","Jue","Vie","Sáb","Dom")

    // --- FASE 1: Pruebas de AppStatisticsContent
    @Test
    fun appStatisticsContentDisplaysItems(){
        composeTestRule.setContent {
            AppStatisticsContent(
                usedApps = testUsedApps
            )
        }
        composeTestRule.onNodeWithText("Apps mas usadas").assertIsDisplayed()
        composeTestRule.onNodeWithText("Facebook").assertIsDisplayed()
        composeTestRule.onNodeWithText("1h 30min").assertIsDisplayed()
        composeTestRule.onNodeWithText("Instagram").assertIsDisplayed()
        composeTestRule.onNodeWithText("45m").assertIsDisplayed()
    }

    // --- FASE 2: Pruebas de WeekStatistics
    @Test
    fun weekStatisticsDisplaysAllElements(){
        val promSemanal = "2h 45min"
        composeTestRule.setContent {
            WeekStatistics(
                values = testDailyHours,
                labels = testWeekLabels,
                promSemanal = promSemanal
            )
        }
        composeTestRule.onNodeWithTag("Prom Semanal").assertIsDisplayed()
        composeTestRule.onNodeWithText(promSemanal).assertIsDisplayed()
        composeTestRule.onNodeWithText("Lun").assertIsDisplayed()
        composeTestRule.onNodeWithText("Dom").assertIsDisplayed()
    }

    // --- FASE 3: Pruebas del Composable Padre AppStatistics
    @Test
    fun appStatisticsTodayView() {
        val useHours = "4h 15min"
        composeTestRule.setContent {
            AppStatistics(
                useHours = useHours,
                usedApps = testUsedApps,
                weeklyAverage = "2h 30min",
                dailyHours = testDailyHours
            )
        }
        composeTestRule.onNodeWithText("Hoy").assertIsDisplayed()
        composeTestRule.onNodeWithText(useHours).assertIsDisplayed()
        composeTestRule.onNodeWithText("Apps mas usadas").assertIsDisplayed()
        composeTestRule.onNodeWithText("Promedio Semanal", substring = true).assertDoesNotExist()
    }
    @Test
    fun appStatisticsSwitchingWeeklyView() {
        val weeklyAverage = "2h 30min"

        composeTestRule.setContent {
            AppStatistics(
                useHours = "4h 15min",
                usedApps = testUsedApps,
                weeklyAverage = weeklyAverage,
                dailyHours = testDailyHours
            )
        }
        composeTestRule.onNodeWithText("Semanal").performClick()
        composeTestRule.onNodeWithTag("Prom Semanal").assertIsDisplayed()
        composeTestRule.onNodeWithText(weeklyAverage).assertIsDisplayed()
        composeTestRule.onNodeWithText("Apps mas usadas").assertDoesNotExist()
    }

    // --- FASE 4: Pruebas de Integración Avanzadas
    @Test
    fun appStatisticsTodayViewZeroUsage() {
        val dataWithZero = listOf(
            UsageStats(appName = "Facebook", usageTime = "0h 0min"),
            UsageStats(appName = "OtraApp", usageTime = "5m")
        )
        composeTestRule.setContent {
            AppStatistics(
                useHours = "5min",
                usedApps = dataWithZero,
                weeklyAverage = "0h",
                dailyHours = emptyList()
            )
        }
        composeTestRule.onNodeWithText("5min").assertIsDisplayed()
        composeTestRule.onNodeWithText("Facebook").assertIsDisplayed()
    }
    @Test
    fun appStatisticsTodayViewNoUsedApps() {
        val noApps = emptyList<UsageStats>()
        composeTestRule.setContent {
            AppStatistics(
                useHours = "0h 0min",
                usedApps = noApps,
                weeklyAverage = "0h",
                dailyHours = emptyList()
            )
        }
        composeTestRule.onNodeWithText("0h 0min").assertIsDisplayed()
        composeTestRule.onNodeWithText("Apps mas usadas").assertIsDisplayed()
    }
    @Test
    fun appStatisticsWeeklyViewNoDailyHours() {
        val noHours = emptyList<Float>()
        val zeroAvg = "0h 0min"
        composeTestRule.setContent {
            AppStatistics(
                useHours = "0h",
                usedApps = testUsedApps,
                weeklyAverage = zeroAvg,
                dailyHours = noHours
            )
        }
        composeTestRule.onNodeWithText("Semanal").performClick()
        composeTestRule.onNodeWithTag("Prom Semanal").assertIsDisplayed()
        composeTestRule.onNodeWithText(zeroAvg).assertIsDisplayed()
        composeTestRule.onNodeWithTag("WeeklyBarChartCanvas").assertIsDisplayed()
        composeTestRule.onNodeWithText("Lun").assertIsDisplayed()
    }
}