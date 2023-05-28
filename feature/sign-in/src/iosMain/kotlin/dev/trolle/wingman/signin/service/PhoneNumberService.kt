package dev.trolle.wingman.signin.service

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.core.scope.Scope


internal actual fun Scope.phoneNumberService(): PhoneNumberService = object : PhoneNumberService {

    override val phoneNumber: MutableSharedFlow<Result<String?>> = MutableSharedFlow()

    @Composable
    override fun Register() = Unit

    override suspend fun getPhoneNumber() = phoneNumber.emit(Result.failure(NotImplementedError()))
}
