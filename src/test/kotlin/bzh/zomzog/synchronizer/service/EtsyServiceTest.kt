package bzh.zomzog.synchronizer.service


import bzh.zomzog.synchronizer.domain.ProductEtsy
import bzh.zomzog.synchronizer.domain.ProductEtsyMongo
import bzh.zomzog.synchronizer.domain.productEtsyMongo
import bzh.zomzog.synchronizer.repository.EtsyRepository
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono

internal class EtsyServiceTest {
    val etsyRepository: EtsyRepository = mockk()

    val etsyClient: EtsyClient = mockk()

    val etsyService = EtsyService(etsyClient, etsyRepository)

    @BeforeEach
    fun init() {
        clearMocks(etsyRepository, etsyClient)
    }


//    @Nested
//    inner class Pull {
//
//        @Test
//        fun `pull from etsy`() {
//            val dontCare: ProductEtsyMongo = mockk()
//            listReturnTwoProducts()
//            firstProductIsInDatabase()
//            secondeProductNotInDatabase()
//
//            every {
//                etsyRepository.save(productEtsyMongo(etsyId = 1337, name="prod1"))
//            } returns Mono.just(dontCare)
//            every {
//                etsyRepository.save(productEtsyMongo(etsyId = 1337, name="prod2"))
//            } returns Mono.just(dontCare)
//
//            val pulled = etsyService.pullFromEtsy()
//            assertThat(pulled).isEqualTo(2)
//        }
//
//        private fun secondeProductNotInDatabase() {
//            every {
//                etsyRepository.findByEtsyId(1)
//            } returns Mono.empty()
//        }
//
//        private fun firstProductIsInDatabase() {
//            every {
//                etsyRepository.findByEtsyId(1337)
//            } returns Mono.just(ProductEtsyMongo(ObjectId.get(), 1337, "name", 3, 0, "href"))
//        }
//
//        private fun listReturnTwoProducts() {
//            every {
//                etsyClient.list(any(), any())
//            } returnsMany listOf(
//                Mono.just(
//                EtsyClient.PagedResult(
//                    2,
//                    listOf(defaultProduct(listing_id = 1337, title = "prod1"), defaultProduct(title = "prod2")),
//                    EtsyClient.Pagination(1, 2, 1, 1)
//                )),
//
//                Mono.just(EtsyClient.PagedResult(
//                    0,
//                    emptyList(),
//                    EtsyClient.Pagination(2, 2, 1, 1)
//                ))
//            )
//        }
//
//    }
//
//    @Nested
//    inner class List {
//        @Test
//        fun `list all empty`() {
//            val result = runBlocking {
//                coEvery {
//                    etsyRepository.getAll()
//                } returns emptyList()
//
//                etsyService.list()
//            }
//            assertThat(result).isEmpty()
//        }
//
//        @Test
//        fun `list all with two products`() {
//            val prod1 = defaultProductEtsy(name= "prod1")
//            val prod2 = defaultProductEtsy(name= "prod2")
//            val result = runBlocking {
//                coEvery {
//                    etsyRepository.getAll()
//                } returns listOf(prod1, prod2)
//
//                etsyService.list()
//            }
//            assertThat(result).containsAll(prod1, prod2)
//        }
//    }
//
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