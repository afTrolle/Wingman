package dev.trolle.wingman.signin.service

import dev.trolle.wingman.signin.service.PhoneValidateService

internal actual fun phoneValidateService() = object : PhoneValidateService {
    override fun isPhoneNumberValid(number: String): Boolean = true
    override fun formatPhoneNumber(number: String): String = number
}
