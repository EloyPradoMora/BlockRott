package com.example.blockrott.appTest

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.blockrott.frontend.components.UsedApps
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UsedAppTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val appName = "Discord"

    @Test
    fun usedAppTest(){
        val usageTime = "1h 45min"
        composeTestRule.setContent {
            UsedApps(
                appName, usageTime
            )
        }
        composeTestRule.onNodeWithTag("used_apps").assertIsDisplayed()
        composeTestRule.onNodeWithText(appName).assertIsDisplayed()
        composeTestRule.onNodeWithText(usageTime).assertIsDisplayed()
    }
}