package bzh.zomzog.synchronizer.etsy.utils

import bzh.zomzog.synchronizer.etsy.domain.ProductEtsy

fun defaultProductEtsy(
    id: Int = 1,
    etsyId: Int = 10,
    name: String = "Rainbow Dash",
    quantity: Int = 0,
    dateUpdated: Long = 9999,
    href: String = "https;//zomzog.fr"
) = ProductEtsy(
    id, etsyId, name, quantity, dateUpdated, href
)
