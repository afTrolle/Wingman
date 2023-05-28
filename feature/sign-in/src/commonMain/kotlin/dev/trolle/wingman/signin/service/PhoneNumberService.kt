package dev.trolle.wingman.signin.service

import androidx.compose.runtime.Composable
import dev.trolle.wingman.common.koin.rememberKoinInject
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.drop
import org.koin.core.scope.Scope

interface PhoneNumberService {
    val phoneNumber: SharedFlow<Result<String?>>

    @Composable
    fun Register()

    suspend fun getPhoneNumber()
}

internal expect fun Scope.phoneNumberService(): PhoneNumberService

@Composable
fun InitPhoneNumberService() {
    val phoneNumberService: PhoneNumberService = rememberKoinInject()
    phoneNumberService.Register()
}
