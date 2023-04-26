package dev.trolle.wingman.sign.`in`.service

interface PhoneValidateService {
    fun isPhoneNumberValid(number: String): Boolean
    fun formatPhoneNumber(number: String): String
}

internal expect fun phoneValidateService(): PhoneValidateService
