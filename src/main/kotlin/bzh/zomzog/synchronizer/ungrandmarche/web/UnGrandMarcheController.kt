package bzh.zomzog.synchronizer.ungrandmarche.web

import bzh.zomzog.synchronizer.ungrandmarche.service.UnGrandMarcheScraper
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
class UnGrandMarcheController(kodein: Kodein) : KodeinController(kodein) {

    val ungrandmarcherScraper : UnGrandMarcheScraper by instance()

    override fun Routing.registerRoutes() {

        get<UnGrandMarcheController.Routes.UnGrandMarche> {
            call.respond(ungrandmarcherScraper.listAllProduct())
        }

    }



    /**
     * A class containing routes annotated with [Location] and implementing [TypedRoute].
     */
    object Routes {
        /**
         * Route for listing users.
         */
        @Location("/unGrandMarche")
        class UnGrandMarche : TypedRoute

    }
}

