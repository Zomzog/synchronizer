package bzh.zomzog.synchronizer.repository

import bzh.zomzog.synchronizer.domain.ProductEtsyMongo
import kotlinx.coroutines.reactive.flow.asFlow
import org.springframework.data.mongodb.core.ReactiveFluentMongoOperations
import org.springframework.data.mongodb.core.awaitOne
import org.springframework.data.mongodb.core.insert
import org.springframework.data.mongodb.core.oneAndAwait
import org.springframework.data.mongodb.core.query
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.query.where
import org.springframework.stereotype.Repository

@Repository
class EtsyRepository(private val mongo: ReactiveFluentMongoOperations) {

    suspend fun findAll() = mongo.query<ProductEtsyMongo>().all().asFlow()

    suspend fun findByEtsyId(etsyId: Int) = mongo.query<ProductEtsyMongo>()
        .matching(query(where(ProductEtsyMongo::etsyId).isEqualTo(etsyId))).awaitOne()

    suspend fun save(productEtsyMongo: ProductEtsyMongo) = mongo.insert<ProductEtsyMongo>().oneAndAwait(productEtsyMongo)

}