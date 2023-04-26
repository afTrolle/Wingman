@file:JvmName("PhoneNumberServiceKtJvm")

package dev.trolle.wingman.sign.`in`.service

import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import dev.trolle.wingman.common.ext.runCatchingCancelable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope

internal fun phoneNumberService(context: Context): PhoneNumberService =
    object : PhoneNumberService {

        private lateinit var launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>
        override val phoneNumber = MutableStateFlow<Result<String?>?>(null)

        @Composable
        override fun Register() {
            launcher = rememberLauncherForActivityResult(
                ActivityResultContracts.StartIntentSenderForResult(),
            ) { result ->
                val catching = runCatching {
                    context.signInClient.getPhoneNumberFromIntent(result.data)
                }
                phoneNumber.value = catching
            }
        }

        override suspend fun getPhoneNumber() {
            runCatchingCancelable {
                val request = GetPhoneNumberHintIntentRequest.builder().build()
                val result = context.signInClient
                    .getPhoneNumberHintIntent(request)
                    .await()
                val input = IntentSenderRequest.Builder(result).build()
                launcher.launch(input)
            }.onFailure {
                phoneNumber.value = Result.failure(it)
            }
        }

        private val Context.signInClient: SignInClient get() = Identity.getSignInClient(this)
    }

internal actual fun Scope.phoneNumberService(): PhoneNumberService {
    return phoneNumberService(androidContext())
}
