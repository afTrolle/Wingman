package dev.trolle.af.wingman.service

import androidx.compose.runtime.Composable
import dev.trolle.af.wingman.koin.rememberKoinInject
import kotlinx.coroutines.flow.SharedFlow

interface PhoneNumberService {
    val phoneNumberState: SharedFlow<String?>

    @Composable
    fun ProvidePhoneNumberContext(content: @Composable () -> Unit)

    @Composable
    fun TriggerPhoneNumberFetch(key: Any)
}

@Composable
fun ProvidePhoneNumberContext(content: @Composable () -> Unit) {
    val phoneNumberService: PhoneNumberService = rememberKoinInject()
    phoneNumberService.ProvidePhoneNumberContext(content)
}
