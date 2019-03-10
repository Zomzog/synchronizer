package bzh.zomzog.synchronizer.product.repository

import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import bzh.zomzog.synchronizer.etsy.domain.ProductEtsyEntity
import bzh.zomzog.synchronizer.product.domain.NewProduct
import bzh.zomzog.synchronizer.product.domain.Product
import bzh.zomzog.synchronizer.product.domain.ProductEntity
import bzh.zomzog.synchronizer.product.domain.ProductTable
import bzh.zomzog.synchronizer.service.DatabaseFactory
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ProductRepositoryTest {
    val repository = ProductRepository()

    @BeforeEach
    fun setUp() {
        DatabaseFactory.init()
    }

    @AfterEach
    fun tearDown() {
        transaction {
            SchemaUtils.drop(ProductTable)
        }
    }

    @Nested
    inner class GetAll {
        @Test
        fun `get multiple products`() {

            val first = transaction {
                defaultProductEntity(name = "product1", quantity = 3)
            }
            val second = transaction {
                defaultProductEntity(name = "product2", quantity = 6)
            }
            val expected1 = defaultProduct(id = first.id.value, quantity = 3, name = "product1")
            val expected2 = defaultProduct(id = second.id.value, quantity = 6, name ="product2")

            val all = runBlocking {
                repository.getAll()
            }
            assertThat(all).containsAll(expected1, expected2)
        }

        @Test
        fun `get no products`() {
            runBlocking {
                assertThat(repository.getAll()).isEmpty()
            }
        }
    }

   @Nested
   inner class GetOne {
       @Test
       fun `get existing product`() {
           val existing = transaction {
               ProductEntity.new {
                   name = "pony"
                   dateUpdated = 2
                   quantity = 3
               }
           }
           val result = runBlocking {
               repository.getOne(existing.id.value)
           }
           val expected = defaultProduct(id = existing.id.value, quantity = 3, name = "pony", dateUpdated = 2)
           assertThat(result).isNotNull().isEqualTo(expected)
       }

       @Test
       fun `get non existing product`() {
           val result = runBlocking {
               repository.getOne(1337)
           }
           assertThat(result).isNull()
       }
   }

   @Nested
   inner class Update {

       @Test
       fun `update existing product`() {
           val existing = transaction {
               defaultProductEntity(
                   name = "pony",
                   dateUpdated = 2,
                   quantity = 3
               )
           }

           val update = defaultNewProduct(id = existing.id.value, quantity = 10, name = "Rainbow Dash")

           val result = runBlocking {
               repository.update(update)
           }
           val expected = defaultProduct(id = existing.id.value, quantity = 10, name = "Rainbow Dash")
           org.assertj.core.api.Assertions.assertThat(result).isEqualToIgnoringGivenFields(expected, "dateUpdated") // FIXME change for assertk
       }

       @Test
       fun `update product without id create a new one`() {
           val update = defaultNewProduct(id = null, quantity = 10, name = "Rainbow Dash")

           val result = runBlocking {
               repository.update(update)
           }
           val expected =
               defaultProduct(id = result!!.id, quantity = 10, name = "Rainbow Dash", dateUpdated = result.dateUpdated)
           assertThat(result).isEqualTo(expected)
       }

       @Test
       fun `update non existing product throw exception`() {
           val update = NewProduct(9, 10, "Rainbow Dash", 0)

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
               val saved = repository.add(defaultNewProduct(name= "name",  quantity = 2))

               assertThat(saved.id).isNotNull()
           }
       }

       @Test
       fun `add ignore product id`() {
           runBlocking {
               val saved = repository.add(defaultNewProduct(id=999, name="name", quantity = 2))

               assertThat(saved.id).isNotNull().isNotEqualTo(999)
           }
       }
   }

   @Nested
   inner class Delete {
       @Test
       fun `delete existing`() {
           val existing = transaction {
               ProductEntity.new {
                   name = "delete"
                   dateUpdated = 2
                   quantity = 3
               }
           }
           runBlocking {
               repository.delete(existing.id.value)
           }
           transaction {
               val all = ProductEntity.all()
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
               val all = ProductEntity.all()
               // TODO use assertk when 0.14
               org.assertj.core.api.Assertions.assertThat(all).isEmpty()
           }
       }
   }

    fun defaultProductEntity(
        id: Int? = null,
        etsyProduct: ProductEtsyEntity? = null,
        name: String = "name",
        quantity: Int = 42,
        dateUpdated: Long = 1337
    ): ProductEntity {
        return ProductEntity.new(id) {
            this.etsyProduct = etsyProduct
            this.name = name
            this.quantity = quantity
            this.dateUpdated = dateUpdated
        }
    }

    fun defaultProduct(
        id: Int = 1,
        etsyId: Int? = null,
        name: String = "name",
        quantity: Int = 42,
        dateUpdated: Long = 1337
    ): Product {
        return Product(id, etsyId, name, quantity, dateUpdated)
    }

    fun defaultNewProduct(
        id: Int? = 1,
        etsyId: Int? = null,
        name: String = "name",
        quantity: Int = 42
    ): NewProduct {
        return NewProduct(id, etsyId, name, quantity)
    }
}