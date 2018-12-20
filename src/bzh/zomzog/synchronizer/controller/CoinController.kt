package bzh.zomzog.synchronizer.controller

import bzh.zomzog.bzh.zomzog.synchronizer.domain.TickerRequest
import bzh.zomzog.synchronizer.service.CoinService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.routing
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class CoinController(override val kodein: Kodein) : KodeinAware {

    val app: Application by instance()
    val service: CoinService by instance("coinService")

    init {
        app.routing {
            get<TickerRequest> { request ->
                call.respond(service.find(request.symbol, request.start.atStartOfDay(), request.end.atTime(23, 59, 59)))
            }
        }
    }
}