package bzh.zomzog.synchronizer.product.domain

import bzh.zomzog.synchronizer.etsy.domain.ProductEtsyEntity
import bzh.zomzog.synchronizer.etsy.domain.ProductEtsyTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object ProductTable : IntIdTable() {
    val etsyProduct = reference("etsyProduct", ProductEtsyTable).nullable()
    val name = varchar("name", 255)
    val quantity = integer("quantity")
    val dateUpdated = long("dateUpdated")
}

class ProductEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductEntity>(ProductTable)

    var etsyProduct by ProductEtsyEntity optionalReferencedOn ProductTable.etsyProduct
    var name by ProductTable.name
    var quantity by ProductTable.quantity
    var dateUpdated by ProductTable.dateUpdated

    fun toProduct(): Product {
        return Product(id.value, etsyProduct?.id?.value, name, quantity, dateUpdated)
    }
}

data class Product(
    val id: Int,
    val etsyId : Int?,
    val name: String,
    val quantity: Int,
    val dateUpdated: Long
)

data class NewProduct(
    val id: Int?,
    val etsyId : Int?,
    val name: String,
    val quantity: Int
)

data class DiffResult(val allUnGrandMarche: List<String>, val allEtsy: List<String>, val leftUnGrandMarche: List<String>, val leftEtsy: List<String>)
