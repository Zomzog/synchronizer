package bzh.zomzog.synchronizer.web

import io.ktor.application.call
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respondText
import io.ktor.routing.Routing
import org.kodein.di.Kodein


class HealthController(kodein: Kodein) : KodeinController(kodein) {
    override fun Routing.registerRoutes() {
        get<HealthController.Routes.Health> {
            call.respondText("OK")
        }
    }

    /**
     * A class containing routes annotated with [Location] and implementing [TypedRoute].
     */
    object Routes {

        @Location("/health")
        class Health : TypedRoute
    }
}