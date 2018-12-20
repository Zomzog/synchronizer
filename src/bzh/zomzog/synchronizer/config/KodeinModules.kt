package bzh.zomzog.synchronizer.config

import bzh.zomzog.synchronizer.controller.CoinController
import bzh.zomzog.synchronizer.repository.CoinRepository
import bzh.zomzog.synchronizer.service.CoinService
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.eagerSingleton
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.litote.kmongo.async.KMongo

val common = Kodein.Module(name = "common") {
    bind("mongoClient") from singleton { KMongo.createClient() }
}

val repositories = Kodein.Module(name = "repositories") {
    bind(tag = "coinRepository") from singleton { CoinRepository(instance("mongoClient")) }
}

val services = Kodein.Module(name = "services") {
    bind(tag="coinService") from singleton { CoinService(instance("coinRepository")) }
}

val controllers = Kodein.Module(name = "controllers") {
    bind(tag="coinController") from eagerSingleton { CoinController(kodein) }
}