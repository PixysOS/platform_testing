/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server.wm.flicker.service

import android.content.ComponentName
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.android.server.wm.flicker.helpers.StandardAppHelper
import com.android.server.wm.flicker.helpers.wakeUpAndGoToHomeScreen
import com.android.server.wm.flicker.rules.WMFlickerServiceRule
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

/**
 * Contains a rotation mock test for [WMFlickerServiceRule].
 *
 * To run this test: `atest FlickerLibTest:RotationMockTest`
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RotationMockTest {
    private val DUMMY_APP = ComponentName("com.google.android.apps.messaging",
            "com.google.android.apps.messaging.ui.ConversationListActivity")

    val instrumentation = InstrumentationRegistry.getInstrumentation()

    @get:Rule
    val rule = WMFlickerServiceRuleTest()

    @Test
    fun startServiceTest() {
        val device = UiDevice.getInstance(instrumentation)

        device.wakeUpAndGoToHomeScreen()
        StandardAppHelper(instrumentation, "dummyApp", DUMMY_APP).launchViaIntent()
        device.waitForIdle()
        device.setOrientationLeft()
        instrumentation.uiAutomation.syncInputTransactions()
        device.setOrientationNatural()
        instrumentation.uiAutomation.syncInputTransactions()
    }
}
