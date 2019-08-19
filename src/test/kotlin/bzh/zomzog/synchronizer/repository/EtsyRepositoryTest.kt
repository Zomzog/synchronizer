package bzh.zomzog.synchronizer.repository

import bzh.zomzog.synchronizer.domain.ProductEtsyMongo
import bzh.zomzog.synchronizer.domain.productEtsyMongo
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.MongoTemplate
import reactor.test.StepVerifier

//@DataMongoTest
internal class EtsyRepositoryTest {

    @Autowired
    lateinit var repository: EtsyRepository

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
        mongoTemplate.dropCollection(ProductEtsyMongo::class.java)
    }

//    @Nested
//    inner class FindAll {
//        @Test
//        fun `get multiple products`() {
//
//            val first = repository.save(
//                productEtsyMongo(
//                    name = "getAll1"
//                )
//            ).block()
//
//            val second = repository.save(
//                productEtsyMongo(
//                    etsyId = 4,
//                    name = "getAll2",
//                    dateUpdated = 5,
//                    href = "href2",
//                    quantity = 6
//                )
//            ).block()
//
//            assertThat(first).isNotNull
//            assertThat(second).isNotNull
//
//            val expected1 = ProductEtsyMongo(first!!.id, 1, "getAll1", 3, 2, "href")
//            val expected2 = ProductEtsyMongo(second!!.id, 4, "getAll2", 6, 5, "href2")
//
//
//            StepVerifier.create(repository.findAll())
//                .assertNext { assertThat(it).isEqualTo(expected1) }
//                .assertNext { assertThat(it).isEqualTo(expected2) }
//                .expectComplete()
//                .verify()
//        }
//
//        @Test
//        fun `get no products`() {
//            StepVerifier.create(repository.findAll())
//                .expectComplete()
//                .verify()
//        }
//    }
//
//
//    @Nested
//    inner class GetOne {
//        @Test
//        fun `get existing product`() {
//            val existing = repository.save(productEtsyMongo()).block()
//
//            val expected = productEtsyMongo()
//
//            StepVerifier.create(repository.findById(existing!!.id))
//                .assertNext { assertThat(it).isEqualToIgnoringGivenFields(expected, "id") }
//                .expectComplete()
//                .verify()
//        }
//    }
//
//    @Test
//    fun `get non existing product`() {
//
//        StepVerifier.create(repository.findById(ObjectId.get()))
//            .expectComplete()
//            .verify()
//    }

//   @Test
//   fun `get one by etsyId`() {
//       val existing = transaction {
//           ProductEtsyEntity.new {
//               etsyId = 99
//               name = "pony"
//               dateUpdated = 2
//               href = "href"
//               quantity = 3
//           }
//       }
//       val result = runBlocking {
//           repository.getOneByEtsyId(99)
//       }
//       val expected = ProductEtsy(existing.id.value, 99, "pony", 3, 2, "href")
//       assertThat(result).isNotNull().isEqualTo(expected)
//   }

//   @Test
//   fun `get non existing  by etsyId`() {
//       val result = runBlocking {
//           repository.getOneByEtsyId(99)
//       }
//       assertThat(result).isNull()
//   }
//
//    @Nested
//    inner class FindByEtsyId {
//        @Test
//        fun `not found`() {
//            StepVerifier.create(repository.findByEtsyId(11))
//                .expectComplete()
//                .verify()
//        }
//
//        @Test
//        fun `existing one`() {
//            val existing = repository.save(productEtsyMongo(etsyId = 42)).block()
//
//            StepVerifier.create(repository.findByEtsyId(42))
//                .assertNext {
//                    assertThat(it).isEqualTo(existing)
//                }
//                .expectComplete()
//                .verify()
//        }
//    }

/*
   @Nested
   inner class Update {

       @Test
       fun `update existing product`() {
           val existing = transaction {
               ProductEtsyEntity.new {
                   etsyId = 1
                   name = "pony"
                   dateUpdated = 2
                   href = "href"
                   quantity = 3
               }
           }

           val update = NewProductEtsy(existing.id.value, 10, "Rainbow Dash", 0, "https;//zomzog.fr")

           val result = runBlocking {
               repository.update(update)
           }
           val expected =
               ProductEtsy(existing.id.value, 10, "Rainbow Dash", 0, result.dateUpdated, "https;//zomzog.fr")
           assertThat(result).isEqualTo(expected)
       }

       @Test
       fun `update product without id create a new one`() {
           val update = NewProductEtsy(null, 10, "Rainbow Dash", 0, "https;//zomzog.fr")

           val result = runBlocking {
               repository.update(update)
           }
           val expected =
               ProductEtsy(result.id, 10, "Rainbow Dash", 0, result.dateUpdated, "https;//zomzog.fr")
           assertThat(result).isEqualTo(expected)
       }

       @Test
       fun `update non existing product throw exception`() {
           val update = NewProductEtsy(9, 10, "Rainbow Dash", 0, "https;//zomzog.fr")

           assertThrows(Exception::class.java) {
               runBlocking {
                   repository.update(update)
               }
           }
       }
   }

   @Nested
   inner class Add {
       @Test
       fun `add new product`() {
           runBlocking {
               val saved = repository.add(NewProductEtsy(null, 1, "name", 2, "href"))

               assertThat(saved.id).isNotNull()
           }
       }

       @Test
       fun `add ignore product id`() {
           runBlocking {
               val saved = repository.add(NewProductEtsy(999, 1, "name", 2, "href"))

               assertThat(saved.id).isNotNull().isNotEqualTo(999)
           }
       }
   }

   @Nested
   inner class Delete {
       @Test
       fun `delete existing`() {
           val existing = transaction {
               ProductEtsyEntity.new {
                   etsyId = 1
                   name = "delete"
                   dateUpdated = 2
                   href = "href"
                   quantity = 3
               }
           }
           runBlocking {
               repository.delete(existing.id.value)
           }
           transaction {
               val all = ProductEtsyEntity.all()

               // TODO use assertk when 0.14
               org.assertj.core.api.Assertions.assertThat(all).isEmpty()
           }
       }

       @Test
       fun `delete non existing won't throw errors`() {
           runBlocking {
               repository.delete(1337)
           }
           transaction {
               val all = ProductEtsyEntity.all()
               // TODO use assertk when 0.14
               org.assertj.core.api.Assertions.assertThat(all).isEmpty()
           }
       }
   }*/
}
