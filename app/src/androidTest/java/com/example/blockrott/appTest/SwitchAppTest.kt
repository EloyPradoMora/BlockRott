package com.example.blockrott.appTest

import androidx.compose.runtime.mutableIntStateOf
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
import junit.framework.TestCase
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
    fun switchAppTest_InitialStateOff(){
        val initialState = false
        val selectedApps = mutableStateListOf<String>()
        composeTestRule.setContent {
            SwitchApp(
                appName, initialState, selectedApps
            )
        }
        composeTestRule.onNodeWithText(appName).assertIsDisplayed()
        composeTestRule.onNodeWithTag("switch_tag").assertIsOff()
        TestCase.assertFalse("NO debe contener la app al inicio", selectedApps.contains(appName))
        composeTestRule.onNodeWithTag("switch_tag").performClick()
        composeTestRule.onNodeWithTag("switch_tag").assertIsOn()
        TestCase.assertTrue("Debe contener la app", selectedApps.contains(appName))
    }

    // --- 2. Lógica de Eliminación (Inicialmente Activado) ---

    @Test
    fun switchAppTest_InitialStateOn() {
        val selectedApps = mutableStateListOf(appName)
        val initialChecked = true

        composeTestRule.setContent {
            SwitchApp(
                appName = appName,
                initialChecked = initialChecked,
                selectedApps = selectedApps
            )
        }
        composeTestRule.onNodeWithTag("switch_tag").assertIsOn()
        TestCase.assertTrue("Debe contener la app al inicio",selectedApps.contains(appName))
        composeTestRule.onNodeWithTag("switch_tag").performClick()
        composeTestRule.onNodeWithTag("switch_tag").assertIsOff()
        TestCase.assertFalse("NO debe contener appName después del click",selectedApps.contains(appName)
        )
    }
}