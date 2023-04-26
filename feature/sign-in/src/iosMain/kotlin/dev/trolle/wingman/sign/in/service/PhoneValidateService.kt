package dev.trolle.wingman.sign.`in`.service

internal actual fun phoneValidateService() = object : PhoneValidateService {
    override fun isPhoneNumberValid(number: String): Boolean = true
    override fun formatPhoneNumber(number: String): String = number
}
