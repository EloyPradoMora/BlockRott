package com.example.blockrott

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.runner.AndroidJUnit4
import com.example.blockrott.frontend.components.DailyPieChart
import com.example.blockrott.frontend.components.PieChartEntry
import com.example.blockrott.frontend.components.WeeklyBarChart
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GraphicsTest {
    @get:Rule
    val graphicsTestRule = createComposeRule()

    @Test
    fun weeklyBarChart_displaysLabelsCorrectly(){
        val values = listOf(10f, 20f, 5f)
        val labels = listOf("Lun", "Mar", "Mié")

        graphicsTestRule.setContent {
            WeeklyBarChart(values = values, labels = labels)
        }
        graphicsTestRule.onNodeWithTag("WeeklyBarChartCanvas").assertIsDisplayed()
        graphicsTestRule.onNodeWithTag("WeeklyBarChartLabels").assertIsDisplayed()

        graphicsTestRule.onNodeWithText("Lun").assertIsDisplayed()
        graphicsTestRule.onNodeWithText("Mar").assertIsDisplayed()
    }
    @Test
    fun dailyPieChart_withData_rendersCanvas(){
        val data = listOf(
            PieChartEntry(percentage = 50.0, color = Color.Red),
        )
        graphicsTestRule.setContent {
            DailyPieChart(
                data = data,
                modifier = Modifier.size(200.dp)
            )
        }
        graphicsTestRule.onNodeWithTag("DailyPieChartCanvas").assertIsDisplayed()
    }
    @Test
    fun dailyPieChart_withEmptyData_doesNotCrash(){
        val data = emptyList<PieChartEntry>()
        graphicsTestRule.setContent {
            DailyPieChart(
                data = data,
                modifier = Modifier.size(200.dp)
            )
        }
        graphicsTestRule.onNodeWithTag("DailyPieChartCanvas").assertIsDisplayed()
    }
}