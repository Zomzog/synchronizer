package bzh.zomzog.synchronizer.repository

import bzh.zomzog.synchronizer.domain.ProductEtsyMongo
import org.bson.types.ObjectId
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface EtsyRepository : ReactiveCrudRepository<ProductEtsyMongo, ObjectId>