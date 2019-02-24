package bzh.zomzog.synchronizer.etsy.web

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import bzh.zomzog.synchronizer.bindSingleton
import bzh.zomzog.synchronizer.etsy.domain.ProductEtsy
import bzh.zomzog.synchronizer.etsy.service.EtsyService
import bzh.zomzog.synchronizer.etsy.utils.defaultProductEtsy
import bzh.zomzog.synchronizer.module
import io.ktor.config.MapApplicationConfig
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

internal class EtsyControllerTest {
    val etsyService: EtsyService = mockk()

    @Test
    fun `pull endpoint call pull service`()  = testApp {
        coEvery { etsyService.pullFromEtsy() } returns 2

        handleRequest(HttpMethod.Get, "/etsy/pull").apply {
            assertThat(response.status()).isNotNull().isEqualTo(HttpStatusCode.OK)
            assertThat(response.content).isNotNull().isEqualTo("2")
        }
    }

    @Test
    fun `list know etsy product`()  = testApp {
        val prod1 : ProductEtsy = defaultProductEtsy(name = "prod1")
        val prod2 : ProductEtsy = defaultProductEtsy(name = "prod2")
        coEvery { etsyService.list() } returns listOf(prod1, prod2)

        handleRequest(HttpMethod.Get, "/etsy").apply {
            assertThat(response.status()).isNotNull().isEqualTo(HttpStatusCode.OK)
            assertThat(response.content).isNotNull().contains("prod1")
            assertThat(response.content).isNotNull().contains("prod2")
        }
    }

    /**
     * Convenience method we use to configure a test application and to execute a [callback] block testing it.
     */
    private fun testApp(callback: TestApplicationEngine.() -> Unit): Unit {
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
        bindSingleton { EtsyController(it) }
        bind<EtsyService>() with singleton { etsyService }
    }
}