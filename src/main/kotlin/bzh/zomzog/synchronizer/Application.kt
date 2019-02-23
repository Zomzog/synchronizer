package bzh.zomzog.synchronizer

import bzh.zomzog.synchronizer.bzh.zomzog.synchronizer.config.SynchronizerProperties
import bzh.zomzog.synchronizer.etsy.web.EtsyController
import bzh.zomzog.synchronizer.product.repository.ProductRepository
import bzh.zomzog.synchronizer.product.service.ProductService
import bzh.zomzog.synchronizer.product.web.ProductController
import bzh.zomzog.synchronizer.service.DatabaseFactory
import bzh.zomzog.synchronizer.ungrandmarche.service.UnGrandMarcheScraper
import bzh.zomzog.synchronizer.ungrandmarche.web.UnGrandMarcheController
import bzh.zomzog.synchronizer.web.KodeinController
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.KtorExperimentalAPI
import io.ktor.websocket.WebSockets
import org.kodein.di.Instance
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.kodein.di.jvmType

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Application.module(
    kodeinMapper: Kodein.MainBuilder.(Application) -> Unit = {}
) {

    val application = this

    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }
    install(CallLogging)
    install(WebSockets)
    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
        }
    }
    install(Locations)

    DatabaseFactory.init()

    val etsyApiKey = environment.config.config("synchronizer").property("etsyApiKey").getString()
    val properties = SynchronizerProperties(etsyApiKey)

    /**
     * Creates a [Kodein] instance, binding the [Application] instance.
     * Also calls the [kodeInMapper] to map the Controller dependencies.
     */
    val kodein = Kodein {
        bind<Application>() with instance(application)
        bind<SynchronizerProperties>() with instance(properties)
        kodeinMapper(this, application)
    }

    /**
     * Detects all the registered [KodeinController] and registers its routes.
     */
    routing {
        for (bind in kodein.container.tree.bindings) {
            val bindClass = bind.key.type.jvmType as? Class<*>?
            if (bindClass != null && KodeinController::class.java.isAssignableFrom(bindClass)) {
                val res by kodein.Instance(bind.key.type)
                println("Registering '$res' routes...")
                (res as KodeinController).apply { registerRoutes() }
            }
        }
    }
}

@KtorExperimentalLocationsAPI
@KtorExperimentalAPI
fun main(args: Array<String>) {

    embeddedServer(Netty, port = 8888) {
        module {
            bindJackson()

            bindProduct()
            bindUnGrandMarche()
            bindEtsy()
        }
    }.start(wait = true)
}


@KtorExperimentalLocationsAPI
private fun Kodein.MainBuilder.bindProduct() {
    bindSingleton { ProductRepository() }
    bindSingleton { ProductService(it) }
    bindSingleton { ProductController(it) }
}

@KtorExperimentalLocationsAPI
private fun Kodein.MainBuilder.bindUnGrandMarche() {
    bindSingleton { UnGrandMarcheController(it) }
    bindSingleton { UnGrandMarcheScraper(it) }
}

@KtorExperimentalLocationsAPI
private fun Kodein.MainBuilder.bindEtsy() {
    bindSingleton { EtsyController(it) }
}

private fun Kodein.MainBuilder.bindJackson() {
    bindSingleton {
        jacksonObjectMapper().apply {
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
    }
}

/**
 * Shortcut for binding singletons to the same type.
 */
inline fun <reified T : Any> Kodein.MainBuilder.bindSingleton(crossinline callback: (Kodein) -> T) {
    bind<T>() with singleton { callback(this@singleton.kodein) }
}