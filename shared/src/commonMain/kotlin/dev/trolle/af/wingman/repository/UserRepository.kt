package dev.trolle.af.wingman.repository

import dev.trolle.af.wingman.service.PersistenceService
import dev.trolle.app.service.tinder.TinderService

interface UserRepository {
    suspend fun startSignIn(phoneNumber: String)

    val userIsSignedIn: Boolean

}

fun userRepository(
    tinderService: TinderService,
    persistenceService: PersistenceService,
) = object : UserRepository {

    override suspend fun startSignIn(phoneNumber: String) {
        tinderService.otp(phoneNumber)
    }

    override val userIsSignedIn: Boolean get() = false

}