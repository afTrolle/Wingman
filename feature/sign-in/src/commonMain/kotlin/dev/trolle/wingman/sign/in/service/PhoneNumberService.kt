package dev.trolle.wingman.sign.`in`.service

import androidx.compose.runtime.Composable
import dev.trolle.wingman.common.koin.rememberKoinInject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.drop
import org.koin.core.scope.Scope

interface PhoneNumberService {
    val phoneNumber: StateFlow<Result<String?>?>

    @Composable
    fun Register()

    suspend fun getPhoneNumber()
}

internal val PhoneNumberService.shouldFetchPhoneNumber get() = phoneNumber.value == null
internal val PhoneNumberService.numberUpdates get() = phoneNumber.drop(1)

internal expect fun Scope.phoneNumberService(): PhoneNumberService

@Composable
fun InitPhoneNumberService() {
    val phoneNumberService: PhoneNumberService = rememberKoinInject()
    phoneNumberService.Register()
}
