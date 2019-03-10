package bzh.zomzog.synchronizer.web

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import bzh.zomzog.synchronizer.bindSingleton
import bzh.zomzog.synchronizer.module
import io.ktor.config.MapApplicationConfig
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.junit.jupiter.api.Test
import org.kodein.di.Kodein

internal class HealthControllerTest {

    @Test
    fun `health check OK`() = testApp {
        handleRequest(HttpMethod.Get, "/health").apply {
            assertThat(response.status()).isNotNull().isEqualTo(HttpStatusCode.OK)
            assertThat(response.content).isNotNull().isEqualTo("OK")
        }
    }

    /**
     * Convenience method we use to configure a test application and to execute a [callback] block testing it.
     */
    private fun testApp(callback: TestApplicationEngine.() -> Unit) {
        withTestApplication({
            (environment.config as MapApplicationConfig).apply {
                put("synchronizer.etsyApiKey", "pony")
            }
            module {
                bindController()
            }
        }, callback)
    }

    private fun Kodein.MainBuilder.bindController() {
        bindSingleton { HealthController(it) }
    }
}