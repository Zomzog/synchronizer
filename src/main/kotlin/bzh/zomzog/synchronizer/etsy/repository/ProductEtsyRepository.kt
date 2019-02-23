package bzh.zomzog.synchronizer.etsy.repository

import bzh.zomzog.synchronizer.etsy.domain.NewProductEtsy
import bzh.zomzog.synchronizer.etsy.domain.ProductEtsy
import bzh.zomzog.synchronizer.etsy.domain.ProductEtsyEntity
import bzh.zomzog.synchronizer.etsy.domain.ProductEtsyTable
import bzh.zomzog.synchronizer.service.DatabaseFactory.dbQuery

class ProductEtsyRepository {

    suspend fun getAll(): List<ProductEtsy> = dbQuery {
        ProductEtsyEntity.all().map { it.toProduct() }
    }

    suspend fun getOne(id: Int): ProductEtsy? =
        dbQuery {
            ProductEtsyEntity.findById(id)
        }?.toProduct()

    suspend fun getOneByEtsyId(etsyId: Int): ProductEtsy? =
        dbQuery {
            ProductEtsyEntity.find { ProductEtsyTable.etsyId eq etsyId }.firstOrNull()
        }?.toProduct()

    suspend fun update(product: NewProductEtsy): ProductEtsy {
        val id = product.id
        return if (id == null) {
            add(product)
        } else {
            dbQuery {
                val entity = ProductEtsyEntity.findById(id)
                if (null == entity) {
                    throw Exception("Product not found")
                } else {
                    entity.etsyId = product.etsyId
                    entity.name = product.name
                    entity.quantity = product.quantity
                    entity.dateUpdated = System.currentTimeMillis()
                    entity.href = product.href
                    entity.toProduct()
                }
            }
        }
    }

    suspend fun add(product: NewProductEtsy): ProductEtsy {
        return dbQuery {
            val saved = ProductEtsyEntity.new {
                etsyId = product.etsyId
                name = product.name
                quantity = product.quantity
                dateUpdated = System.currentTimeMillis()
                href = product.href
            }
            saved.toProduct()
        }
    }

    suspend fun delete(id: Int) {
        return dbQuery {
            ProductEtsyEntity.findById(id)?.delete()
        }
    }
}