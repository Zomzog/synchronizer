package bzh.zomzog.synchronizer.etsy.web

import bzh.zomzog.synchronizer.etsy.service.EtsyService
import bzh.zomzog.synchronizer.web.KodeinController
import bzh.zomzog.synchronizer.web.TypedRoute
import io.ktor.application.call
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Routing
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class EtsyController(kodein: Kodein) : KodeinController(kodein) {

    val etsyService: EtsyService by instance()

    override fun Routing.registerRoutes() {

        get<EtsyController.Routes.List> {
            call.respond(etsyService.list())
        }

        get<EtsyController.Routes.Pull> {
            call.respond(etsyService.pullFromEtsy())
        }
    }

    /**
     * A class containing routes annotated with [Location] and implementing [TypedRoute].
     */
    object Routes {

        @Location("/etsy")
        class List : TypedRoute

        @Location("/etsy/pull")
        class Pull : TypedRoute
    }
}
