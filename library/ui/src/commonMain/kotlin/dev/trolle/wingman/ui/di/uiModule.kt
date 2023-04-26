package dev.trolle.wingman.ui.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dev.trolle.wingman.common.koin.rememberKoinInject
import dev.trolle.wingman.common.koin.rememberKoinInjectOrNull
import dev.trolle.wingman.ui.string.LocalStrings
import dev.trolle.wingman.ui.string.StringsDefinition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.dsl.bind
import org.koin.dsl.module


fun uiModule(
    initialStrings: StringsDefinition,
) = module {
    single { MutableStateFlow(initialStrings) }.bind<StateFlow<StringsDefinition>>()
}

// Call after koin has been injected
@Composable
 fun UpdateStrings() {
    val stringsFlow = rememberKoinInjectOrNull<MutableStateFlow<StringsDefinition>>()
    val strings = LocalStrings.current
    LaunchedEffect(strings) {
        stringsFlow?.emit(strings)
    }
}