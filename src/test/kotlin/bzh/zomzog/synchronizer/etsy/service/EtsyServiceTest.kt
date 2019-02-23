package bzh.zomzog.synchronizer.etsy.service

import assertk.assertThat
import assertk.assertions.isEqualTo
import bzh.zomzog.synchronizer.bzh.zomzog.synchronizer.etsy.service.EtsyClient
import bzh.zomzog.synchronizer.bzh.zomzog.synchronizer.etsy.service.EtsyService
import bzh.zomzog.synchronizer.etsy.domain.NewProductEtsy
import bzh.zomzog.synchronizer.etsy.domain.ProductEtsy
import bzh.zomzog.synchronizer.etsy.repository.ProductEtsyRepository
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

internal class EtsyServiceTest {
    val etsyRepository: ProductEtsyRepository = mockk()

    val etsyClient: EtsyClient = mockk()

    val etsyService = EtsyService(Kodein {
        bind<ProductEtsyRepository>() with singleton { etsyRepository }
        bind<EtsyClient>() with singleton { etsyClient }
    })

    @BeforeEach
    fun init() {
        clearMocks(etsyRepository, etsyClient)
    }

    @Test
    fun `pull from etsy`() {
        val dontCare: ProductEtsy = mockk()
        listReturnTwoProducts()
        firstProductIsInDatabase()
        secondeProductNotInDatabase()

        coEvery {
            etsyRepository.update(NewProductEtsy(42, 1337, "prod1", 42, "http://zomzog.fr"))
        } returns dontCare
        coEvery {
            etsyRepository.add(NewProductEtsy(null, 1, "prod2", 42, "http://zomzog.fr"))
        } returns dontCare

        val pulled = runBlocking {
            etsyService.pullFromEtsy()
        }
        assertThat(pulled).isEqualTo(2)
    }

    private fun secondeProductNotInDatabase() {
        coEvery {
            etsyRepository.getOneByEtsyId(1)
        } returns null
    }

    private fun firstProductIsInDatabase() {
        coEvery {
            etsyRepository.getOneByEtsyId(1337)
        } returns ProductEtsy(42, 1337, "name", 3, 0, "href")
    }

    private fun listReturnTwoProducts() {
        coEvery {
            etsyClient.list(any(), any())
        } returnsMany listOf(
            EtsyClient.PagedResult(
                2,
                listOf(defaultProduct(listing_id = 1337, title = "prod1"), defaultProduct(title = "prod2")),
                EtsyClient.Pagination(1, 2, 1, 1)
            ),

            EtsyClient.PagedResult(
                0,
                emptyList(),
                EtsyClient.Pagination(2, 2, 1, 1)
            )
        )
    }

    fun defaultProduct(
        listing_id: Int = 1,
        title: String = "title",
        description: String = "description",
        price: String = "1.5",
        url: String = "http://zomzog.fr",
        state: String = "active",
        quantity: Int = 42
    ) = EtsyClient.Result(listing_id, title, description, price, url, state, quantity)
}