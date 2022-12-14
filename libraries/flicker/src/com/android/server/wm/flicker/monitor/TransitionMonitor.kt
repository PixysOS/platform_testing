/*
 * Copyright (C) 2020 The Android Open Source Project
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

package com.android.server.wm.flicker.monitor

import com.android.server.wm.flicker.FlickerRunResult
import java.nio.file.Files
import java.nio.file.Path

abstract class TransitionMonitor(
    outputDir: Path,
    sourceFile: Path
) : TraceMonitor(outputDir, sourceFile) {
    /**
     * Acquires the trace generated when executing the commands defined in the [predicate].
     *
     * @param predicate Commands to execute
     * @throws UnsupportedOperationException If tracing is already activated
     */
    fun withTracing(predicate: () -> Unit): ByteArray {
        if (this.isEnabled) {
            throw UnsupportedOperationException("Trace already running. " +
                    "This is likely due to chained 'withTracing' calls.")
        }
        try {
            this.start()
            predicate()
        } finally {
            this.stop()
        }

        val builder = FlickerRunResult.Builder()
        builder.setResultFrom(this)

        return outputFile.let {
            Files.readAllBytes(it).also { _ -> Files.delete(it) }
        } ?: error("Unable to acquire trace")
    }
}
