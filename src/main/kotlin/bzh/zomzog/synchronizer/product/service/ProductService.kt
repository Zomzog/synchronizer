package bzh.zomzog.synchronizer.product.service

import bzh.zomzog.synchronizer.product.domain.DiffResult
import bzh.zomzog.synchronizer.ungrandmarche.service.UnGrandMarcheScraper
import bzh.zomzog.synchronizer.etsy.web.EtsyController
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class ProductService(override val kodein: Kodein) : KodeinAware {

    val etsyController: EtsyController by instance()

    val unGrandMarcherScraper: UnGrandMarcheScraper by instance()

    suspend fun diff(): DiffResult {
        val etsy = emptyList<String>()
        // val etsy = etsyController.updateFromEtsy()
        val ugm = unGrandMarcherScraper.listProduct(1)
        val leftEtsy = etsy.toMutableList()
        val leftugm = ugm.toMutableList()

        for (e in leftEtsy.toMutableList()) {
            if (leftugm.contains(e)) {
                leftEtsy.remove(e)
                leftugm.remove(e)
            }
        }

        return DiffResult(etsy, ugm, leftEtsy, leftugm)
    }
}