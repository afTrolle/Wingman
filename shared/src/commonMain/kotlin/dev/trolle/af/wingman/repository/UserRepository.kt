package dev.trolle.af.wingman.repository

import dev.trolle.af.wingman.service.PersistenceService
import dev.trolle.af.wingman.service.TinderService

interface UserRepository {
    fun startSignIn(phoneNumber: String) {
        TODO("Not yet implemented")
    }

    val userIsSignedIn: Boolean

}

fun userRepository(
    tinderService: TinderService,
    persistenceService: PersistenceService,
) = object : UserRepository {

    override val userIsSignedIn: Boolean get() = false

}