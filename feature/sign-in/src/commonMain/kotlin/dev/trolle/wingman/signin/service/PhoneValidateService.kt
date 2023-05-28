package dev.trolle.wingman.signin.service

interface PhoneValidateService {
    fun isPhoneNumberValid(number: String): Boolean
    fun formatPhoneNumber(number: String): String
}

internal expect fun phoneValidateService(): PhoneValidateService
