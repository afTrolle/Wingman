package dev.trolle.af.wingman.service


actual fun phoneValidateService() = object : PhoneValidateService {
    override fun isPhoneNumberValid(number: String): Boolean = true
    override fun formatPhoneNumber(number: String): String = number
}