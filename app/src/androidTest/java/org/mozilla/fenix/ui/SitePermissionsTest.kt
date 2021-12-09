/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.ui

import androidx.core.net.toUri
import androidx.test.rule.GrantPermissionRule
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mozilla.fenix.helpers.AndroidAssetDispatcher
import org.mozilla.fenix.helpers.HomeActivityIntentTestRule
import org.mozilla.fenix.ui.robots.browserScreen
import org.mozilla.fenix.ui.robots.navigationToolbar

/**
 *  Tests for verifying site permissions prompts & functionality
 *
 */
class SitePermissionsTest {
    private lateinit var mockWebServer: MockWebServer

    @get:Rule
    val activityTestRule = HomeActivityIntentTestRule()

    @get:Rule
    var mGrantPermissions = GrantPermissionRule.grant(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.RECORD_AUDIO,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    @Before
    fun setUp() {
        mockWebServer = MockWebServer().apply {
            dispatcher = AndroidAssetDispatcher()
            start()
        }
    }

    @Test
    fun denyMicrophonePermissionPromptTest() {
        val webRTCtestPage = "https://mozilla.github.io/webrtc-landing/gum_test.html"
        val testPageSubstring = "https://mozilla.github.io:443"

        navigationToolbar {
        }.enterURLAndEnterToBrowser(webRTCtestPage.toUri()) {
        }.clickStartMicrophoneButton {
            verifyMicrophonePermissionPrompt(testPageSubstring)
        }.clickPagePermissionButton(false) {
            verifyPageContent("NotAllowedError")
        }
    }

    @Test
    fun allowMicrophonePermissionPromptTest() {
        val webRTCtestPage = "https://mozilla.github.io/webrtc-landing/gum_test.html"
        val testPageSubstring = "https://mozilla.github.io:443"

        navigationToolbar {
        }.enterURLAndEnterToBrowser(webRTCtestPage.toUri()) {
        }.clickStartMicrophoneButton {
            verifyMicrophonePermissionPrompt(testPageSubstring)
        }.clickPagePermissionButton(true) {
            verifyPageContent("Success!")
        }
    }

    @Test
    fun denyCameraPermissionPromptTest() {
        val testPage = "https://mozilla.github.io/webrtc-landing/gum_test.html"
        val testPageSubstring = "https://mozilla.github.io:443"

        navigationToolbar {
        }.enterURLAndEnterToBrowser(testPage.toUri()) {
        }.clickStartCameraButton {
            verifyCameraPermissionPrompt(testPageSubstring)
        }.clickPagePermissionButton(false) {
            verifyPageContent("NotAllowedError")
        }
    }

    @Test
    fun allowCameraPermissionPromptTest() {
        val testPage = "https://mozilla.github.io/webrtc-landing/gum_test.html"
        val testPageSubstring = "https://mozilla.github.io:443"

        navigationToolbar {
        }.enterURLAndEnterToBrowser(testPage.toUri()) {
        }.clickStartCameraButton {
            verifyCameraPermissionPrompt(testPageSubstring)
        }.clickPagePermissionButton(true) {
            verifyPageContent("Success!")
        }
    }

    @Test
    fun cameraAndMicPermissionPromptTest() {
        val testPage = "https://mozilla.github.io/webrtc-landing/gum_test.html"
        val testPageSubstring = "https://mozilla.github.io:443"

        navigationToolbar {
        }.enterURLAndEnterToBrowser(testPage.toUri()) {
        }.clickStartCameraAndMicrophoneButton { }
        browserScreen {
        }.clickStartCameraAndMicrophoneButton {
            verifyCameraAndMicPermissionPrompt(testPageSubstring)
        }.clickPagePermissionButton(false) {
            verifyPageContent("NotAllowedError")
        }.clickStartCameraAndMicrophoneButton {
        }.clickPagePermissionButton(true) {
            verifyPageContent("Success!")
        }
    }
}