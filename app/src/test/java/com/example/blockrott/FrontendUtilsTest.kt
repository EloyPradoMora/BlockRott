package com.example.blockrott

import com.example.blockrott.frontend.theme.*
import com.example.blockrott.frontend.utils.FrontendUtils
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FrontendUtilsTest {
    private lateinit var frontendUtils: FrontendUtils
    @Before
    fun setup(){
        frontendUtils = FrontendUtils()
    }

    // --- totalMinutes ---
    @Test
    fun totalMinutesExpected(){
        val input = "2h 30min"
        val expected = 150.0
        val actual = frontendUtils.totalMinutes(input)
        assert(expected == actual)
    }
    @Test
    fun totalMinutesOnlyHours(){
        val input = "1h 5h"
        val expected = 360.0
        val actual = frontendUtils.totalMinutes(input)
        assert(expected == actual)
    }
    @Test
    fun totalMinutesWithDifferentFormats() {
        val input = "45min 10m"
        val expected = 55.0
        val actual = frontendUtils.totalMinutes(input)
        assert(expected == actual)
    }
    @Test
    fun totalMinutes_withInvalidParts_ignoresInvalidAndReturnsCorrectTotal() {
        val input = "1h abc 20min 5x"
        val expected = 80.0
        val actual = frontendUtils.totalMinutes(input)
        assert(expected == actual)
    }
    @Test
    fun totalMinutes_emptyString_returnsZero() {
        val input = ""
        val expected = 0.0
        val actual = frontendUtils.totalMinutes(input)
        assert(expected == actual)
    }

    // --- Pruebas para colorApp ---
    @Test
    fun colorApp_facebookApp_returnsFacebookColor() {
        val appName = "com.android.app.Facebook"
        val actualColor = frontendUtils.colorApp(appName)
        assert(Facebook == actualColor)
    }
    @Test
    fun colorApp_instagramApp_returnsInstagramColor() {
        val appName = "Instagram"
        val actualColor = frontendUtils.colorApp(appName)
        assert(Instagram == actualColor)
    }
    @Test
    fun colorApp_tiktokApp_returnsTikTokColor() {
        val appName = "TikTok-Video-App"
        val actualColor = frontendUtils.colorApp(appName)
        assert(TikTok == actualColor)
    }
    @Test
    fun colorApp_youtubeApp_returnsYoutubeColor() {
        val appName = "MyTubeApp-YouTube"
        val actualColor = frontendUtils.colorApp(appName)
        assert(Youtube == actualColor)
    }
    @Test
    fun colorApp_discordApp_returnsDiscordColor() {
        val appName = "Discord (chat)"
        val actualColor = frontendUtils.colorApp(appName)
        assert(Discord == actualColor)
    }
    @Test
    fun colorApp_redditApp_returnsRedditColor() {
        val appName = "official.Reddit"
        val actualColor = frontendUtils.colorApp(appName)
        assert(Reddit == actualColor)
    }
    @Test
    fun colorApp_unknownApp_returnsTimerBackground() {
        val appName = "A-Different-App"
        val actualColor = frontendUtils.colorApp(appName)
        assert(TimerBackground == actualColor)
    }
}