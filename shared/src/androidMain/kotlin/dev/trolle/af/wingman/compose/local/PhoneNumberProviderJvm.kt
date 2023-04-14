package dev.trolle.af.wingman.compose.local

import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await

private val Context.signInClient: SignInClient get() = Identity.getSignInClient(this)

class PhoneNumberProviderImpl(
    override val phoneNumber: MutableStateFlow<Result<String?>?>,
    private val launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
    private val context: Context
) : PhoneNumberProvider {
    override suspend fun fetchPhoneNumber() {
        // This will fail if you don't have play services ie google play store.
        kotlin.runCatching {
            val request = GetPhoneNumberHintIntentRequest.builder().build()
            val result = context.signInClient
                .getPhoneNumberHintIntent(request)
                .await()
            val input = IntentSenderRequest.Builder(result).build()
            launcher.launch(input)
        }.onFailure {
            if (it is CancellationException) throw it
        }
    }
}

@Composable
actual fun SetLocalPhoneNumberProvider(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val phoneNumberState = MutableStateFlow<Result<String?>?>(null)
    val launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult> =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            val catching = runCatching {
                context.signInClient.getPhoneNumberFromIntent(result.data)
            }
            phoneNumberState.value = catching
        }

    val phoneNumberProvider = PhoneNumberProviderImpl(phoneNumberState, launcher, context)
    CompositionLocalProvider(LocalPhoneNumberProvider provides phoneNumberProvider) {
        content()
    }
}
