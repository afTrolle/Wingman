package dev.trolle.af.wingman.service

import com.google.i18n.phonenumbers.PhoneNumberUtil


actual fun phoneValidateService() = object : PhoneValidateService {

    private val phoneUtil = PhoneNumberUtil.getInstance()

    override fun isPhoneNumberValid(number: String): Boolean =
        kotlin.runCatching {
            val phoneNumber = phoneUtil.parse(number, null)
            phoneUtil.isValidNumber(phoneNumber)
        }.getOrNull() ?: false

    override fun formatPhoneNumber(number: String): String {
        val phoneNumber = phoneUtil.parse(number, null)
        return phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
    }

}