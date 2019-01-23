package bzh.zomzog.synchronizer.web

/**
 * Created by Zomzog on 29/12/2018.
 */
import bzh.zomzog.synchronizer.bzh.zomzog.synchronizer.web.KodeinController
import bzh.zomzog.synchronizer.bzh.zomzog.synchronizer.web.TypedRoute
import bzh.zomzog.synchronizer.domain.NewWidget
import bzh.zomzog.synchronizer.service.WidgetService
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.http.cio.websocket.Frame
import io.ktor.locations.*
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.websocket.webSocket
import org.kodein.di.Kodein
import org.kodein.di.generic.instance


class WidgetController(kodein: Kodein) : KodeinController(kodein) {
    val widgetService : WidgetService by instance()

    val mapper : ObjectMapper by instance()

    /**
     * Registers the routes related to [Users].
     */
    override fun Routing.registerRoutes() {
        /**
         * GET route for [Routes.Users] /users, it responds
         * with a HTML listing all the users in the repository.
         */
        get<Routes.Widgets> {
            call.respond(widgetService.getAllWidgets())
        }

        post<Routes.Widgets> {
            val widget = call.receive<NewWidget>()
            call.respond(HttpStatusCode.Created, widgetService.addWidget(widget))
        }

        put<Routes.Widgets> {
            val widget = call.receive<NewWidget>()
            val updated = widgetService.updateWidget(widget)
            if(updated == null) call.respond(HttpStatusCode.NotFound)
            else call.respond(HttpStatusCode.OK, updated)
        }

        get<Routes.Widget> {
            val widget = widgetService.getWidget(call.parameters["id"]?.toInt()!!)
            if (widget == null) call.respond(HttpStatusCode.NotFound)
            else call.respond(widget)
        }

        delete<Routes.Widget> {
            val removed = widgetService.deleteWidget(call.parameters["id"]?.toInt()!!)
            if (removed) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound)
        }

        webSocket("/updates") {
            try {
                widgetService.addChangeListener(this.hashCode()) {
                    outgoing.send(Frame.Text(mapper.writeValueAsString(it)))
                }
                while(true) {
                    incoming.receiveOrNull() ?: break
                }
            } finally {
                widgetService.removeChangeListener(this.hashCode())
            }
        }

    }


    /**
     * A class containing routes annotated with [Location] and implementing [TypedRoute].
     */
    object Routes {
        /**
         * Route for listing users.
         */
        @Location("/widgets")
        class Widgets : TypedRoute

        /**
         * Route for showing a specific widget from its [id].
         */
        @Location("/widgets/{id}")
        data class Widget(val id: String) : TypedRoute
    }
}