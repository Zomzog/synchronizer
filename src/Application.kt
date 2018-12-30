package bzh.zomzog

import bzh.zomzog.synchronizer.web.widget
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.content.*
import io.ktor.http.content.*
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.locations.*

fun main(args: Array<String>) = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
        }
    }
    install(Routing) {
        widget(WidgetService())
    }
}