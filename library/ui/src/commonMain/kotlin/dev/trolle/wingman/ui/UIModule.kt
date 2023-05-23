package dev.trolle.wingman.ui

import androidx.compose.ui.text.intl.Locale
import cafe.adriel.lyricist.LanguageTag
import cafe.adriel.lyricist.Strings
import dev.trolle.wingman.ui.string.Locales
import dev.trolle.wingman.ui.string.StringsDefinition
import org.koin.dsl.module

val uiModule = module {
    single { StringsContainer() }
}

class StringsContainer {
    private val translations = Strings

    // NOTE don't hold reference
    val strings: StringsDefinition
        get() {
            val languageTag = Locale.current.toLanguageTag()
            return translations[languageTag]
                ?: translations[languageTag.fallback]
                ?: translations[defaultLanguageTag]
                ?: error("Strings for language tag $languageTag not found")
        }

    private val LanguageTag.fallback: LanguageTag
        get() = split(LANGUAGE_TAG_SEPARATOR).first()

    companion object {
        private const val LANGUAGE_TAG_SEPARATOR = '-'
        private const val defaultLanguageTag: LanguageTag = Locales.EN
    }
}
