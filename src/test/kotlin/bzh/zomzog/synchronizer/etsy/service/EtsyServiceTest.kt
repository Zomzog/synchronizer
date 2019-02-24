package bzh.zomzog.synchronizer.etsy.service

import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import bzh.zomzog.synchronizer.etsy.domain.NewProductEtsy
import bzh.zomzog.synchronizer.etsy.domain.ProductEtsy
import bzh.zomzog.synchronizer.etsy.repository.ProductEtsyRepository
import bzh.zomzog.synchronizer.etsy.utils.defaultProductEtsy
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
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


    @Nested
    inner class Pull {
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

    }

    @Nested
    inner class List {
        @Test
        fun `list all empty`() {
            val result = runBlocking {
                coEvery {
                    etsyRepository.getAll()
                } returns emptyList()

                etsyService.list()
            }
            assertThat(result).isEmpty()
        }

        @Test
        fun `list all with two products`() {
            val prod1 = defaultProductEtsy(name= "prod1")
            val prod2 = defaultProductEtsy(name= "prod2")
            val result = runBlocking {
                coEvery {
                    etsyRepository.getAll()
                } returns listOf(prod1, prod2)

                etsyService.list()
            }
            assertThat(result).containsAll(prod1, prod2)
        }
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