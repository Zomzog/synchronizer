package bzh.zomzog.synchronizer.domain

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.time.ZoneOffset

data class ProductEtsy(
    val id: String? = null,
    val etsyId: Int,
    val name: String,
    val quantity: Int,
    val href: String
)

@Document
data class ProductEtsyMongo(
    @Id val id: ObjectId = ObjectId.get(),
    val etsyId: Int,
    val name: String,
    val quantity: Int,
    val dateUpdated: Long = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
    val href: String
) {
    fun toProductEtsy() = ProductEtsy(id.toString(), etsyId, name, quantity, href)

    companion object Factory {
        fun fromProductEtsy(product: ProductEtsy): ProductEtsyMongo {
            return if (null == product.id) {
                ProductEtsyMongo(etsyId = product.etsyId, name=product.name, quantity = product.quantity, href = product.href)
            } else {
                ProductEtsyMongo(ObjectId(product.id), etsyId = product.etsyId, name=product.name, quantity = product.quantity, href = product.href)
            }
        }
    }
}