package dev.trolle.wingman.signin

import dev.trolle.wingman.ui.CommonParcelable
import dev.trolle.wingman.ui.CommonParcelize
import kotlin.jvm.JvmInline

sealed interface OneTimePasswordConfig : CommonParcelable {

    @JvmInline
    @CommonParcelize
    value class Email constructor(val email: String) : OneTimePasswordConfig

    @JvmInline
    @CommonParcelize
    value class Phone constructor(val phone: String) : OneTimePasswordConfig

}

