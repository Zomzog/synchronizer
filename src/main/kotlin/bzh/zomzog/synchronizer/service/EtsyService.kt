package bzh.zomzog.synchronizer.service

import org.springframework.stereotype.Service

@Service
class EtsyService(val etsyClient: EtsyClient) {

//    suspend fun pullFromEtsy(): Int {
//
//        var updateCount = 0
//        var offset = 0
//        while (true) {
//            val pagedResult = etsyClient.list(offset, 100)
//            if (pagedResult.map {
//
//                }.results.isEmpty()) {
//                break
//            }
//            updateCount += pagedResult.results.size
//            pagedResult.results
//                .map { it.toProductEtsy() }
//                .map { insertOrUpdate(it) }
//            offset += 100
//            println("updateFromEtsy $offset")
//        }
//        return updateCount
//    }
//
//    private suspend fun insertOrUpdate(product: NewProductEtsy): ProductEtsy {
//        val existing = etsyRepository.getOneByEtsyId(product.etsyId)
//        return if (existing == null) {
//            etsyRepository.add(product)
//        } else {
//            val updated = product.copy(id = existing.id)
//            etsyRepository.update(updated)
//        }
//    }
//
//    suspend fun list(): List<ProductEtsy> = etsyRepository.getAll()
}