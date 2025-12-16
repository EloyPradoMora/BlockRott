package com.example.blockrott.midContent

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.blockrott.frontend.components.BlockConfig
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BlockConfigTest {
    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun blockConfig_integration_selectApps_setMinutes_andConfirm() {
        val appsList = listOf("Discord", "Instagram", "TikTok")
        val timeList = listOf(
            com.example.blockrott.frontend.components.TimeConfigItem("5 Min", 300000L),
            com.example.blockrott.frontend.components.TimeConfigItem("10 Min", 600000L),
            com.example.blockrott.frontend.components.TimeConfigItem("15 Min", 900000L)
        )
        var confirmCalled = false

        composeTestRule.setContent {
            BlockConfig(
                appsList = appsList,
                timeList = timeList,
                onClickConfirm = { _, _ -> confirmCalled = true }
            )
        }

        composeTestRule.onNodeWithText("Apps").assertExists()
        composeTestRule.onNodeWithText("Discord").assertIsDisplayed()
        composeTestRule.onNodeWithText("Instagram").assertIsDisplayed()

        composeTestRule.onNodeWithTag("app_switch_Discord").performClick()
        composeTestRule.onNodeWithTag("app_switch_TikTok").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Min/Seg").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("5 Min").assertExists()
        composeTestRule.onNodeWithText("10 Min").assertExists()
        composeTestRule.onNodeWithText("15 Min").assertExists()

        composeTestRule.onNodeWithTag("time_switch_10 Min").performClick()

        composeTestRule.onNodeWithText("Siguiente").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Confirmar").assertExists()
        composeTestRule.onNodeWithText("Confirmar").performClick()
        composeTestRule.waitForIdle()

        assertTrue("El callback Confirmar NO fue llamado", confirmCalled)
    }
}