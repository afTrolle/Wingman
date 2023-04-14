package dev.trolle.af.wingman.compose.local

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.flow.StateFlow

val LocalPhoneNumberProvider = compositionLocalOf<PhoneNumberProvider?> { null }

interface PhoneNumberProvider {
    val phoneNumber: StateFlow<Result<String?>?>

    suspend fun fetchPhoneNumber()
}

@Composable
expect fun SetLocalPhoneNumberProvider(content: @Composable () -> Unit)