package bzh.zomzog.synchronizer.etsy.service

import bzh.zomzog.synchronizer.etsy.domain.NewProductEtsy
import bzh.zomzog.synchronizer.etsy.domain.ProductEtsy
import bzh.zomzog.synchronizer.etsy.repository.ProductEtsyRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class EtsyService(override val kodein: Kodein) : KodeinAware {

    val etsyRepository: ProductEtsyRepository by instance()

    val etsyClient: EtsyClient by instance()

    suspend fun pullFromEtsy(): Int {

        var updateCount = 0
        var offset = 0
        while (true) {
            val pagedResult = etsyClient.list(offset, 100)
            if (pagedResult.results.isEmpty()) {
                break
            }
            updateCount += pagedResult.results.size
            pagedResult.results
                .map { it.toProductEtsy() }
                .map { insertOrUpdate(it) }
            offset += 100
            println("updateFromEtsy $offset")
        }
        return updateCount
    }

    private suspend fun insertOrUpdate(product: NewProductEtsy): ProductEtsy {
        val existing = etsyRepository.getOneByEtsyId(product.etsyId)
        return if (existing == null) {
            etsyRepository.add(product)
        } else {
            val updated = product.copy(id = existing.id)
            etsyRepository.update(updated)
        }
    }
}