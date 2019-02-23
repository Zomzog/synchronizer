package bzh.zomzog.synchronizer.etsy.web

import bzh.zomzog.synchronizer.bzh.zomzog.synchronizer.etsy.service.EtsyService
import bzh.zomzog.synchronizer.web.KodeinController
import bzh.zomzog.synchronizer.web.TypedRoute
import io.ktor.application.call
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Routing
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

@KtorExperimentalLocationsAPI
class EtsyController(kodein: Kodein) : KodeinController(kodein) {

    val etsyService: EtsyService by instance()

    override fun Routing.registerRoutes() {

        get<EtsyController.Routes.Etsy> {
            call.respond(etsyService.pullFromEtsy())
        }
    }

    /**
     * A class containing routes annotated with [Location] and implementing [TypedRoute].
     */
    object Routes {
        /**
         * Route for listing users.
         */
        @Location("/etsy")
        class Etsy : TypedRoute
    }
}
