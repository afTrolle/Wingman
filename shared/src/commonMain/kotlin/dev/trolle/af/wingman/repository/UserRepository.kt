package dev.trolle.af.wingman.repository

import dev.trolle.af.wingman.service.PersistenceService
import dev.trolle.af.wingman.service.TinderService

interface UserRepository {

    val userIsSignedIn: Boolean

}

fun userRepository(
    tinderService: TinderService,
    persistenceService: PersistenceService,
) = object : UserRepository {

    override val userIsSignedIn: Boolean get() = false

}