package bzh.zomzog.synchronizer.ungrandmarche.service

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import io.ktor.client.request.url
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.net.URL

class UnGrandMarcheScraper(override val kodein: Kodein) : KodeinAware {

    val parser: UgmPageParser by instance()

    suspend fun listAllProduct(): List<String> {

        val productsList = mutableListOf<String>()
        var page = 51
        while (true) {
            val productOfThePage = listProduct(page)

            if (productOfThePage.isEmpty())
                break
            productsList.addAll(productOfThePage)
            page++
            println("$page : ${productsList.size}")
        }

        println("CLIENT: Total of products: ${productsList.size}")
        productsList.sort()
        return productsList
    }

    suspend fun listProduct(page: Int): List<String> {
        val client = HttpClient(Apache)

        val html = client.get<String> {
            url(URL("https://www.ungrandmarche.fr/boutique/petite-coccinelle?page=$page&tri=prix_asc"))
        }

        client.close()

        return parser.extractProductFromHtml(html).map { it.name }
    }
}