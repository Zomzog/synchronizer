package bzh.zomzog.synchronizer.product.repository

import bzh.zomzog.synchronizer.product.domain.NewProduct
import bzh.zomzog.synchronizer.product.domain.Product
import bzh.zomzog.synchronizer.product.domain.ProductTable
import bzh.zomzog.synchronizer.service.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class ProductRepository {

    suspend fun getAll(): List<Product> = dbQuery {
        ProductTable.selectAll().map { toProduct(it) }
    }

    suspend fun getOne(id: Int): Product? = dbQuery {
        ProductTable.select {
            (ProductTable.id eq id)
        }.mapNotNull { toProduct(it) }
            .singleOrNull()
    }

    suspend fun update(product: NewProduct): Product? {
        val id = product.id
        return if (id == null) {
            add(product)
        } else {
            dbQuery {
                ProductTable.update({ ProductTable.id eq id }) {
                    it[name] = product.name
                    it[quantity] = product.quantity
                    it[dateUpdated] = System.currentTimeMillis()
                }
            }
            getOne(id)
        }
    }

    suspend fun add(product: NewProduct): Product {
        var key = 0
        dbQuery {
            key = (ProductTable.insert {
                it[name] = product.name
                it[quantity] = product.quantity
                it[dateUpdated] = System.currentTimeMillis()
            } get ProductTable.id)!!
        }
        return getOne(key)!!
    }

    suspend fun delete(id: Int): Boolean {
        return dbQuery {
            ProductTable.deleteWhere { ProductTable.id eq id } > 0
        }
    }

    private fun toProduct(row: ResultRow): Product =
        Product(
            id = row[ProductTable.id],
            name = row[ProductTable.name],
            quantity = row[ProductTable.quantity],
            dateUpdated = row[ProductTable.dateUpdated]
        )
}