@file:JvmName("ModuleKtJvm")

package dev.trolle.af.wingman.koin

import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import dev.trolle.af.wingman.service.PhoneNumberService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.tasks.await
import org.koin.core.module.Module
import org.koin.dsl.module


// Platform specific module.
actual val platformModule: Module = module {
    single { phoneNumberService() }
}

fun phoneNumberService() = object : PhoneNumberService {
    override val phoneNumberState = MutableSharedFlow<String?>(replay = 1, extraBufferCapacity = 1)
    private val Context.signInClient: SignInClient get() = Identity.getSignInClient(this)

    val LocalLauncher =
        compositionLocalOf<ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>> {
            error("No Launcher found!")
        }

    @Composable
    override fun ProvidePhoneNumberContext(content: @Composable () -> Unit) {
        // Need to check holding context here is safe.
        val context = LocalContext.current
        val launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult> =
            rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                val phoneNumber = try {
                    context.signInClient.getPhoneNumberFromIntent(result.data)
                } catch (e: Exception) {
                    null
                }
                phoneNumberState.tryEmit(phoneNumber)
            }
        CompositionLocalProvider(LocalLauncher provides launcher) {
            content()
        }
    }

    // Trigger fetch phone number intent to launch
    @Composable
    override fun TriggerPhoneNumberFetch(key: Any) {
        val launcher = LocalLauncher.current
        val context = LocalContext.current
        LaunchedEffect(key) {
            kotlin.runCatching {
                val request = GetPhoneNumberHintIntentRequest.builder().build()
                val result = context.signInClient
                    .getPhoneNumberHintIntent(request)
                    .await()
                val input = IntentSenderRequest.Builder(result).build()
                launcher.launch(input)
            }
        }
    }
}
