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
        val timeList = listOf(5, 10, 15)
        var confirmCalled = false

        composeTestRule.setContent {
            BlockConfig(
                appsList = appsList,
                timeList = timeList,
                onClickConfirm = { confirmCalled = true }
            )
        }

        composeTestRule.onNodeWithText("Apps").assertExists()
        composeTestRule.onNodeWithText("Discord").assertIsDisplayed()
        composeTestRule.onNodeWithText("Instagram").assertIsDisplayed()

        composeTestRule.onNodeWithText("Discord").performClick()
        composeTestRule.onNodeWithText("TikTok").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Min").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("row_${timeList[0]}").assertExists()
        composeTestRule.onNodeWithTag("row_${timeList[1]}").assertExists()
        composeTestRule.onNodeWithTag("row_${timeList[2]}").assertExists()

        composeTestRule.onNodeWithTag("row_${timeList[1]}").performClick()

        composeTestRule.onNodeWithText("Siguiente").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Confirmar").assertExists()
        composeTestRule.onNodeWithText("Confirmar").performClick()
        composeTestRule.waitForIdle()

        assertTrue("El callback Confirmar NO fue llamado", confirmCalled)
    }
}