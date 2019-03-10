package bzh.zomzog.synchronizer.product.repository

import bzh.zomzog.synchronizer.etsy.domain.ProductEtsyEntity
import bzh.zomzog.synchronizer.product.domain.NewProduct
import bzh.zomzog.synchronizer.product.domain.Product
import bzh.zomzog.synchronizer.product.domain.ProductEntity
import bzh.zomzog.synchronizer.product.domain.ProductTable
import bzh.zomzog.synchronizer.service.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.deleteWhere

class ProductRepository {

    suspend fun getAll(): List<Product> = dbQuery {
        ProductEntity.all().map { it.toProduct() }
    }

    suspend fun getOne(id: Int): Product? =
        dbQuery {
            ProductEntity.findById(id)
        }?.toProduct()

    suspend fun update(product: NewProduct): Product? {
        val id = product.id
        return if (id == null) {
            add(product)
        } else {
            dbQuery {
                val entity = ProductEntity.findById(id)
                if (null == entity) {
                    throw Exception("Product not found")
                } else {
                    entity.name = product.name
                    val etsyProduct = getEtsyProduct(product.etsyId)
                    entity.etsyProduct = etsyProduct
                    entity.quantity = product.quantity
                    entity.dateUpdated = System.currentTimeMillis()
                    entity.toProduct()
                }
            }
        }
    }

    suspend fun add(product: NewProduct): Product {
        return dbQuery {
            val ep = getEtsyProduct(product.etsyId)
            val saved = ProductEntity.new {
                etsyProduct = ep
                name = product.name
                quantity = product.quantity
                dateUpdated = System.currentTimeMillis()
            }
            saved.toProduct()
        }
    }

    fun getEtsyProduct(etsyId: Int?): ProductEtsyEntity? {
        return if (etsyId != null) {
            ProductEtsyEntity.findById(etsyId) ?: throw IllegalArgumentException("Etsy product not found")
        } else null
    }

    suspend fun delete(id: Int): Boolean {
        return dbQuery {
            ProductTable.deleteWhere { ProductTable.id eq id } > 0
        }
    }
}