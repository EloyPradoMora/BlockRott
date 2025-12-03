package com.example.blockrott.appTest

import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.blockrott.frontend.components.SwitchTime
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SwitchTimeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // --- 1. Lógica de Activación (Inicialmente Desactivado) ---

    @Test
    fun switchTime_clickOn() {
        val timeConfig = 45
        var capturedMin = 0

        composeTestRule.setContent {
            SwitchTime(
                timeConfig = timeConfig,
                initialChecked = false,
                onMinSelected = { newMin -> capturedMin = newMin }
            )
        }
        composeTestRule.onNodeWithTag("switch_tag").assertIsOff()
        assertEquals("El valor inicial debe ser 0.", 0, capturedMin)
        composeTestRule.onNodeWithTag("switch_tag").performClick()
        composeTestRule.onNodeWithTag("switch_tag").assertIsOn()
        assertEquals("El valor DEBE ser igual a timeConfig.", timeConfig, capturedMin)
    }

    // --- 1. Lógica de Desactivación (Inicialmente Activado) ---

    @Test
    fun switchTime_clickOff(){
        val timeConfig = 90
        var capturedMin = 90
        composeTestRule.setContent {
            SwitchTime(
                timeConfig = timeConfig,
                initialChecked = true,
                onMinSelected = { newMin -> capturedMin = newMin }
            )
        }
        composeTestRule.onNodeWithTag("switch_tag").assertIsOn()
        assertEquals("El valor inicial debe ser 90.", timeConfig, capturedMin)
        composeTestRule.onNodeWithTag("switch_tag").performClick()
        composeTestRule.onNodeWithTag("switch_tag").assertIsOff()
        assertEquals("El valor DEBE ser 0.", 0, capturedMin)
    }
}