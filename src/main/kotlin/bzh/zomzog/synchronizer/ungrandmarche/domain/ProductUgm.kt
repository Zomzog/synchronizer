package bzh.zomzog.synchronizer.ungrandmarche.domain

import org.jetbrains.exposed.sql.Table

object ProductUgmTable : Table() {
    val id = ProductUgmTable.integer("id").primaryKey().autoIncrement()
    val ugmId = ProductUgmTable.integer("ugmId")
    val name = ProductUgmTable.varchar("name", 255)
    val dateUpdated = ProductUgmTable.long("dateUpdated")
    val href = ProductUgmTable.long("href")
}

data class ProductUgm(
    val id: Int,
    val ugmId: Int,
    val name: String,
    val dateUpdated: Long,
    val href: String
)

data class NewProductUgm(
    val id: Int?,
    val ugmId: Int,
    val name: String,
    val href: String
)
