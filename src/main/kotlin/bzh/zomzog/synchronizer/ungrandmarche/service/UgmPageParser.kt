package bzh.zomzog.synchronizer.ungrandmarche.service

import bzh.zomzog.synchronizer.ungrandmarche.domain.NewProductUgm
import org.apache.commons.text.StringEscapeUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class UgmPageParser {

    fun extractProductFromHtml(html: String): List<NewProductUgm> {
        val productList: Elements? = Jsoup.parse(html)?.body()?.getElementsByAttributeValue("class", "product_list")
        return if (null == productList || productList.size != 1) {
            emptyList()
        } else {
            productList.get(0).children()
                .filter { containProduct(it) }
                .map { getProductDiv(it) }
                .map { map(it) }
        }
    }

    private fun map(it: Element): NewProductUgm {
        val title = StringEscapeUtils.unescapeHtml4(it.attr("title"))
        val href = it.attr("href")
        val ugmId = href.split("/").last().toInt()
        return NewProductUgm(null, ugmId, title, href)
    }

    private fun getProductDiv(it: Element) = it.child(0)

    private fun containProduct(it: Element) = it.children().size > 0
}