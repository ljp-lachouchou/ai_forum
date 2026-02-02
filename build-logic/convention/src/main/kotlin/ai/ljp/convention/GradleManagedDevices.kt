/*
 * Copyright 2023 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package ai.ljp.convention

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.DeviceGroup
import com.android.build.api.dsl.ManagedVirtualDevice
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.maybeCreate

/**
 * Configure project for Gradle managed devices
 */
internal fun configureGradleManagedDevices(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) { // 对于androidTest定义程序跑在不同设备的测试结果
    //直接用nia的

    // Define device configurations used for local runs and CI.
    //TODO: 后面补UI测试或者性能测试的时候会用到
    val pixel4 = DeviceConfig("Pixel 4", 30, "aosp-atd")
    val pixel6 = DeviceConfig("Pixel 6", 31, "aosp")
    val pixelC = DeviceConfig("Pixel C", 30, "aosp-atd")

    val allDevices = listOf(pixel4, pixel6, pixelC)
    val ciDevices = listOf(pixel4, pixelC)

    commonExtension.testOptions {
        managedDevices {
            devices {
                // Register each managed device by a generated task name.
                allDevices.forEach { deviceConfig ->
                    maybeCreate(deviceConfig.taskName, ManagedVirtualDevice::class.java).apply {
                        device = deviceConfig.device
                        apiLevel = deviceConfig.apiLevel
                        systemImageSource = deviceConfig.systemImageSource
                    }
                }
            }
            groups {
                // CI runs only a subset of devices for faster feedback.
                maybeCreate("ci").apply {
                    ciDevices.forEach { deviceConfig ->
                        targetDevices.add(devices[deviceConfig.taskName])
                    }
                }
            }
        }
    }
}

private data class DeviceConfig(
    val device: String,
    val apiLevel: Int,
    val systemImageSource: String,
) {
    // Build a stable task name like "pixel6api31aosp".
    val taskName = buildString {
        append(device.lowercase().replace(" ", ""))
        append("api")
        append(apiLevel.toString())
        append(systemImageSource.replace("-", ""))
    }
}
