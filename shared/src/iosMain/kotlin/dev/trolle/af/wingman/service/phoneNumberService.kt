package dev.trolle.af.wingman.service

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

 internal fun phoneNumberService(): PhoneNumberService = object : PhoneNumberService {

    override val phoneNumber: StateFlow<Result<String?>?> = MutableStateFlow(Result.success(null))

    @Composable
    override fun Register() = Unit

    override suspend fun getPhoneNumber() = Unit

}