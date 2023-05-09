/*
 * Copyright 2022 The Android Open Source Project
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
@file:RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)

package androidx.paging

import androidx.annotation.IntRange
import androidx.annotation.RestrictTo
import io.github.aakira.napier.LogLevel
import io.github.aakira.napier.Napier

/**
 * Stores an instance of the Logger interface implementation which is to be injected by
 * paging-runtime during runtime. This allows paging-common to add logs with
 * android.util.log as a non-android artifact
 */
public var LOGGER: Logger? = object : Logger {
    val levels = LogLevel.values()
    override fun isLoggable(level: Int): Boolean {
        return Napier.isEnable(levels[level], null)
    }

    override fun log(level: Int, message: String, tr: Throwable?) {
        Napier.log(levels[level], LOG_TAG, tr, message)
    }

}
public const val LOG_TAG: String = "Paging"

//@JvmDefaultWithCompatibility TODO e
/**
 */
public interface Logger {
    public fun isLoggable(level: Int): Boolean
    public fun log(level: Int, message: String, tr: Throwable? = null)
}

/**
 */
public inline fun log(
    @IntRange(from = VERBOSE.toLong(), to = DEBUG.toLong()) level: Int,
    tr: Throwable? = null,
    block: () -> String,
) {
    val logger = LOGGER
    if (logger?.isLoggable(level) == true) {
        logger.log(level, block(), tr)
    }
}

/**
 */
public const val VERBOSE: Int = 2

/**
 */
public const val DEBUG: Int = 3
