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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConfigComponentsTest {
    @get:Rule
    val composeTestRule = createComposeRule()

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