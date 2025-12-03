package com.example.blockrott.midContent

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.blockrott.frontend.components.AppConfig
import com.example.blockrott.frontend.components.TimeConfig
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConfigComponentsTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    // --- PRUEBAS PARA TimeConfig ---

    @Test
    fun timeConfig_displaysAll() {
        val timeList = listOf(15, 30, 60)
        composeTestRule.setContent {
            TimeConfig(
                timeList = timeList,
                currentSelectedMin = 0,
                onMinSelected = {}
            )
        }
        composeTestRule.onNodeWithText("15 min").assertIsDisplayed()
        composeTestRule.onNodeWithText("30 min").assertIsDisplayed()
        composeTestRule.onNodeWithText("60 min").assertIsDisplayed()
    }
    @Test
    fun timeConfig_clickTimeAndCallback(){
        val timeList = listOf(15, 30)
        var capturedMin by mutableIntStateOf(0)
        val tag1 = "tag_${timeList[1]}"
        composeTestRule.setContent {
            TimeConfig(
                timeList = timeList,
                currentSelectedMin = capturedMin,
                onMinSelected = { newMin -> capturedMin = newMin }
            )
        }
        assertEquals(0,capturedMin)
        composeTestRule.onNodeWithTag(tag1).performClick()
        composeTestRule.onNodeWithTag(tag1).assertIsOn()
        assertEquals(30, capturedMin)
    }
    @Test
    fun timeConfig_multiplesClickTimeAndCallback() {
        val timeList = listOf(15, 30)
        var capturedMin by mutableIntStateOf(15)
        val tag0 = "tag_${timeList[0]}"
        val tag1 = "tag_${timeList[1]}"
        composeTestRule.setContent {
            TimeConfig(
                timeList = timeList,
                currentSelectedMin = capturedMin,
                onMinSelected = { newMin -> capturedMin = newMin }
            )
        }
        composeTestRule.onNodeWithTag(tag0).assertIsOn()
        assertEquals(15, capturedMin)
        composeTestRule.onNodeWithTag(tag1).performClick()
        composeTestRule.onNodeWithTag(tag1).assertIsOn()
        composeTestRule.onNodeWithTag(tag0).assertIsOff()
        assertEquals(30, capturedMin)
        composeTestRule.onNodeWithTag(tag1).performClick()
        composeTestRule.onNodeWithTag(tag1).assertIsOff()
        assertEquals(0, capturedMin)
    }

    // --- PRUEBAS PARA AppConfig ---

    @Test
    fun appConfig_addSingleApp_checksIntegration() {
        val appsList = listOf("Discord", "Instagram", "TikTok")
        val selectedApps = mutableStateListOf<String>()
        val appName = appsList[0]

        composeTestRule.setContent {
            AppConfig(
                appsList = appsList,
                selectedApps = selectedApps
            )
        }
        composeTestRule.onNodeWithTag("tag_$appName").performClick()
        composeTestRule.waitForIdle()

        assertEquals(1, selectedApps.size)
        assertEquals(appName, selectedApps[0])
    }
    @Test
    fun appConfig_addMultipleApps() {
        val appsList = listOf("Discord", "Instagram", "TikTok")
        val newList = listOf("Discord","Instagram")
        val selectedApps = mutableStateListOf<String>()

        composeTestRule.setContent {
            AppConfig(
                appsList = appsList,
                selectedApps = selectedApps
            )
        }

        composeTestRule.onNodeWithText("Discord").performClick()
        composeTestRule.onNodeWithText("Instagram").performClick()
        composeTestRule.waitForIdle()

        assertEquals(2, selectedApps.size)
        assertEquals(newList, selectedApps)
    }
    @Test
    fun appConfig_addAndRemoveApp() {
        val appsList = listOf("Discord", "Instagram", "TikTok")
        val selectedApps = mutableStateListOf<String>()
        val target = "Instagram"

        composeTestRule.setContent {
            AppConfig(
                appsList = appsList,
                selectedApps = selectedApps
            )
        }

        composeTestRule.onNodeWithText(target).performClick()
        composeTestRule.waitForIdle()
        assertEquals(1, selectedApps.size)
        assertTrue(selectedApps.contains(target))

        composeTestRule.onNodeWithText(target).performClick()
        composeTestRule.waitForIdle()

        assertTrue(selectedApps.isEmpty())
    }
    @Test
    fun appConfig_addAndRemoveMultipleApps() {
        val appsList = listOf("Discord", "Instagram", "TikTok", "Spotify")
        val selectedApps = mutableStateListOf<String>()
        val toRemove = listOf("Discord", "TikTok")
        val expectedRemaining = listOf("Instagram", "Spotify")

        composeTestRule.setContent {
            AppConfig(
                appsList = appsList,
                selectedApps = selectedApps
            )
        }

        appsList.forEach { app ->
            composeTestRule.onNodeWithText(app).performClick()
        }
        composeTestRule.waitForIdle()
        assertEquals(4, selectedApps.size)

        toRemove.forEach { app ->
            composeTestRule.onNodeWithText(app).performClick()
        }
        composeTestRule.waitForIdle()

        assertEquals(expectedRemaining.size, selectedApps.size)
        assertTrue(selectedApps.containsAll(expectedRemaining))
    }

}