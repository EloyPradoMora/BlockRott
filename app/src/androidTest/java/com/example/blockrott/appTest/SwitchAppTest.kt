package com.example.blockrott.appTest

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.runner.AndroidJUnit4
import com.example.blockrott.frontend.components.SwitchApp
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SwitchAppTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val appName = "Discord"

    // --- 1. Lógica de Adición (Inicialmente Desactivado) ---

    @Test
    fun switchAppTest_InitialStateOff() {
        val appName = "Discord"
        val selectedApps = mutableStateListOf<String>()

        composeTestRule.setContent {
            SwitchApp(
                appName = appName,
                checked = selectedApps.contains(appName),
                onToggle = { app ->
                    if (selectedApps.contains(app)) selectedApps.remove(app)
                    else selectedApps.add(app)
                }
            )
        }
        composeTestRule.onNodeWithText(appName).assertIsDisplayed()
        composeTestRule.onNodeWithTag("tag_$appName").assertIsOff()

        assertFalse(selectedApps.contains(appName))
        composeTestRule.onNodeWithTag("tag_$appName").performClick()

        composeTestRule.onNodeWithTag("tag_$appName").assertIsOn()
        assertTrue(selectedApps.contains(appName))
    }


    // --- 2. Lógica de Eliminación (Inicialmente Activado) ---

    @Test
    fun switchAppTest_InitialStateOn() {
        val appName = "Discord"
        val selectedApps = mutableStateListOf(appName)

        composeTestRule.setContent {
            SwitchApp(
                appName = appName,
                checked = selectedApps.contains(appName),
                onToggle = { app ->
                    if (selectedApps.contains(app)) selectedApps.remove(app)
                    else selectedApps.add(app)
                }
            )
        }
        composeTestRule.onNodeWithTag("tag_$appName").assertIsOn()
        assertTrue(selectedApps.contains(appName))

        composeTestRule.onNodeWithTag("tag_$appName").performClick()
        composeTestRule.onNodeWithTag("tag_$appName").assertIsOff()
        assertFalse(selectedApps.contains(appName))
    }

}