package dev.trolle.af.wingman.service.tinder

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.datetime.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds


// General rate-limit slow-down api calls.
// Ensure that I don't hit backend to often.
internal class RateLimit(
    private val timeBetweenRequests: Duration = 100.milliseconds
) {
    private val timeStampRef = MutableStateFlow(Clock.System.now())

    private fun getSleepTime(): Duration {
        val now = Clock.System.now()
        val timestampWhenToPerformCall = timeStampRef.updateAndGet { tat ->
            val updatedTat = tat + timeBetweenRequests
            maxOf(updatedTat, now)
        }
        return timestampWhenToPerformCall - now
    }

    suspend fun delay() {
        val duration = getSleepTime()
        kotlinx.coroutines.delay(duration)
    }
}