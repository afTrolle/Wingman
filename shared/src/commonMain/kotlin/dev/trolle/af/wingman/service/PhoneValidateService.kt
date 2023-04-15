package dev.trolle.af.wingman.service

interface PhoneValidateService {
    fun isPhoneNumberValid(number: String): Boolean
    fun formatPhoneNumber(number: String): String
}

expect fun phoneValidateService() : PhoneValidateService