package bzh.zomzog.synchronizer.domain

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

data class ProductEtsy(
    val id: String,
    val etsyId: Int,
    val name: String,
    val quantity: Int,
    val dateUpdated: Long,
    val href: String
)

@Document
data class ProductEtsyMongo(
    @Id val id: ObjectId = ObjectId.get(),
    val etsyId: Int,
    val name: String,
    val quantity: Int,
    val dateUpdated: Long,
    val href: String

)