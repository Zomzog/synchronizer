package bzh.zomzog.synchronizer.product.domain

import org.jetbrains.exposed.sql.Table

object ProductTable : Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val name = varchar("name", 255)
    val quantity = integer("quantity")
    val dateUpdated = long("dateUpdated")
}

data class Product(
    val id: Int,
    val name: String,
    val quantity: Int,
    val dateUpdated: Long
)

data class NewProduct(
    val id: Int?,
    val name: String,
    val quantity: Int
)

data class DiffResult(val allUnGrandMarche: List<String>, val allEtsy: List<String>, val leftUnGrandMarche: List<String>, val leftEtsy: List<String>)
