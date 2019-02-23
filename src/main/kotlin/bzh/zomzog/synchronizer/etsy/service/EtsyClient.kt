package bzh.zomzog.synchronizer.bzh.zomzog.synchronizer.etsy.service

import bzh.zomzog.synchronizer.bzh.zomzog.synchronizer.config.SynchronizerProperties
import bzh.zomzog.synchronizer.etsy.domain.NewProductEtsy
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.client.request.url
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.net.URL

class EtsyClient(override val kodein: Kodein) : KodeinAware {

    val properties: SynchronizerProperties by instance()

    suspend fun list(offset: Int, pageSize: Int = 100): PagedResult {
        val ETSY_API_KEY = properties.etsyApiKey
        val client = HttpClient(Apache) {

            install(JsonFeature) {
                serializer = JacksonSerializer {
                    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                }
            }
        }

        val result = client.get<PagedResult> {
            url(URL("https://openapi.etsy.com/v2/shops/15485902/listings/active?api_key=$ETSY_API_KEY&language=fr&limit=$pageSize&offset=$offset&sort_on=price&sort_order=up"))
        }

        client.close()

        return result
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
        fun toProductEtsy() =
            NewProductEtsy(etsyId = listing_id, name = title, quantity = quantity, href = url)
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