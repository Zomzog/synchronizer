package bzh.zomzog.synchronizer.service

import bzh.zomzog.synchronizer.config.SynchronizerProperties
import bzh.zomzog.synchronizer.domain.ProductEtsy
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class EtsyClient(val properties: SynchronizerProperties) {

    suspend fun list(offset: Int, pageSize: Int = 100): PagedResult {
        val ETSY_API_KEY = properties.etsyApiKey
        val client =
            WebClient.create("https://openapi.etsy.com/v2/shops/15485902/listings/active?api_key=$ETSY_API_KEY&language=fr&limit=$pageSize&offset=$offset&sort_on=price&sort_order=up")

        // TODO change this for kotlin
        return client.get().retrieve().bodyToMono(PagedResult::class.java).block()!!
    }

    data class PagedResult(val count: Int, val results: List<Result>, val pagination: Pagination)

    data class Result(
        val listing_id: Int,
        val title: String,
        val description: String,
        val price: String,
        val url: String,
        val state: String,
        val quantity: Int
    ) {
        fun toProductEtsy(): ProductEtsy =
            ProductEtsy(etsyId = listing_id, name = title, quantity = quantity, href = url)
    }

    data class Pagination(
        @JsonProperty("effective_page")
        val effectivePage: Int,
        @JsonProperty("next_page")
        val nextPage: Int,
        @JsonProperty("effective_offset")
        val effectiveOffset: Int,
        @JsonProperty("next_offset")
        val nextOffset: Int
    )
}