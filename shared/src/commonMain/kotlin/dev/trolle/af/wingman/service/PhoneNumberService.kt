package dev.trolle.af.wingman.service

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.drop

internal interface PhoneNumberService {
    val phoneNumber: StateFlow<Result<String?>?>

    @Composable
    fun Register()

    suspend fun getPhoneNumber()
}

internal val PhoneNumberService.shouldFetchPhoneNumber get() = phoneNumber.value == null
internal val PhoneNumberService.numberUpdates get() = phoneNumber.drop(1)
