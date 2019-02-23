package bzh.zomzog.synchronizer.etsy.domain

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object ProductEtsyTable : IntIdTable() {
    val etsyId = integer("etsyId")
    val name = varchar("name", 255)
    val quantity = integer("quantity")
    val dateUpdated = long("dateUpdated")
    val href = varchar("href", 255)
}

class ProductEtsyEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductEtsyEntity>(ProductEtsyTable)

    var etsyId by ProductEtsyTable.etsyId
    var name by ProductEtsyTable.name
    var quantity by ProductEtsyTable.quantity
    var dateUpdated by ProductEtsyTable.dateUpdated
    var href by ProductEtsyTable.href

    fun toProduct(): ProductEtsy {
        return ProductEtsy(id.value, etsyId, name, quantity, dateUpdated, href)
    }
}


data class ProductEtsy(
    val id: Int,
    val etsyId: Int,
    val name: String,
    val quantity: Int,
    val dateUpdated: Long,
    val href: String
)

data class NewProductEtsy(
    val id: Int? = null,
    val etsyId: Int,
    val name: String,
    val quantity: Int,
    val href: String
)
