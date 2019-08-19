package bzh.zomzog.synchronizer.domain


fun productEtsyMongo(
    etsyId: Int = 1,
    name: String = "name",
    dateUpdated: Long = 2,
    href: String = "href",
    quantity: Int = 3
): ProductEtsyMongo {
    return ProductEtsyMongo(
        etsyId = etsyId,
        name = name,
        dateUpdated = dateUpdated,
        href = href,
        quantity = quantity
    )
}