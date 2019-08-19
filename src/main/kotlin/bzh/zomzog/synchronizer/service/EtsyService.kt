package bzh.zomzog.synchronizer.service

import bzh.zomzog.synchronizer.domain.ProductEtsy
import bzh.zomzog.synchronizer.domain.ProductEtsyMongo
import bzh.zomzog.synchronizer.repository.EtsyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service

@Service
class EtsyService(val etsyClient: EtsyClient, val etsyRepository: EtsyRepository) {

    suspend fun pullFromEtsy(): Int {

        var updateCount = 0
        var offset = 0
        while (true) {
            println("updateFromEtsy $offset")
            val pageCount = fetchPage(offset)
            if (pageCount > 0L) {
                offset += 100
                updateCount += fetchPage(offset)
            } else {
                break
            }
        }
        return updateCount
    }

    private suspend fun fetchPage(offset: Int): Int {
        return etsyClient.list(offset, 100)
            .results
            .map { it.toProductEtsy() }
            .map { insertOrUpdate(it) }
            .count()
    }

    private suspend fun insertOrUpdate(product: ProductEtsy): ProductEtsyMongo {
        return etsyRepository.save(ProductEtsyMongo.fromProductEtsy(product))
    }


    suspend fun list(): Flow<ProductEtsy> = etsyRepository.findAll().map { it.toProductEtsy() }
}