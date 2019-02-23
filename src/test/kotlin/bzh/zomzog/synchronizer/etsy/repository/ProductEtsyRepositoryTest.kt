package bzh.zomzog.synchronizer.etsy.repository

import assertk.assertThat
import assertk.assertions.*
import bzh.zomzog.synchronizer.etsy.domain.NewProductEtsy
import bzh.zomzog.synchronizer.etsy.domain.ProductEtsy
import bzh.zomzog.synchronizer.etsy.domain.ProductEtsyEntity
import bzh.zomzog.synchronizer.etsy.domain.ProductEtsyTable
import bzh.zomzog.synchronizer.service.DatabaseFactory
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ProductEtsyRepositoryTest {

    val repository : ProductEtsyRepository = ProductEtsyRepository()

    @BeforeEach
    fun setUp() {
        DatabaseFactory.init()
    }

    @AfterEach
    fun tearDown() {
        transaction {
            SchemaUtils.drop(ProductEtsyTable)
        }
    }

    @Nested
    inner class GetAll {
        @Test
        fun `get multiple products`() {

            val first = transaction {
                ProductEtsyEntity.new {
                    etsyId = 1
                    name = "getAll1"
                    dateUpdated = 2
                    href = "href"
                    quantity = 3
                }
            }
            val second = transaction {
                ProductEtsyEntity.new {
                    etsyId = 4
                    name = "getAll2"
                    dateUpdated = 5
                    href = "href2"
                    quantity = 6
                }
            }
            val expected1 = ProductEtsy(first.id.value, 1, "getAll1", 3, 2, "href")
            val expected2 = ProductEtsy(second.id.value, 4, "getAll2", 6, 5, "href2")

            runBlocking {
                assertThat(repository.getAll()).containsAll(expected1, expected2)
            }
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
                ProductEtsyEntity.new {
                    etsyId = 1
                    name = "pony"
                    dateUpdated = 2
                    href = "href"
                    quantity = 3
                }
            }
            val result = runBlocking {
                repository.getOne(existing.id.value)
            }
            val expected = ProductEtsy(existing.id.value, 1, "pony", 3, 2, "href")
            assertThat(result).isNotNull().isEqualTo(expected)
        }
        @Test
        fun `get non existing product`() {
            val result = runBlocking {
                repository.getOne(1337)
            }
            assertThat(result).isNull()
        }
        @Test
        fun `get one by etsyId`() {
            val existing = transaction {
                ProductEtsyEntity.new {
                    etsyId = 99
                    name = "pony"
                    dateUpdated = 2
                    href = "href"
                    quantity = 3
                }
            }
            val result = runBlocking {
                repository.getOneByEtsyId(99)
            }
            val expected = ProductEtsy(existing.id.value, 99, "pony", 3, 2, "href")
            assertThat(result).isNotNull().isEqualTo(expected)
        }
    }
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
        fun `update non existing product create a new one`() {
            val update = NewProductEtsy(null, 10, "Rainbow Dash", 0, "https;//zomzog.fr")

            val result = runBlocking {
                repository.update(update)
            }
            val expected =
                ProductEtsy(result.id, 10, "Rainbow Dash", 0, result.dateUpdated, "https;//zomzog.fr")
            assertThat(result).isEqualTo(expected)
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
    }
}