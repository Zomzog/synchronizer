package bzh.zomzog.synchronizer.ungrandmarche.service

import bzh.zomzog.synchronizer.ungrandmarche.domain.NewProductUgm
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class UgmPageParserTest {

    @Test
    fun `extract products from page`() {
        val pageContent = UnGrandMarcherScraperTest::class.java.classLoader.getResource("fullpage.html").readText()

        val products = UgmPageParser().extractProductFromHtml(pageContent)

        val requiredProducts = listOf(NewProductUgm(null, 2536166, "Attache tétine silicone, perle koala entier, gris, blanc et rose poudré", "https://www.ungrandmarche.fr/boutiques/p/puericulture/attache-tetine-silicone-perle-koala-entier-gris-blanc-et/2536166"),
        NewProductUgm(null, 1595306, "Hochet arc avec anneau et grelot, rose et violet", "https://www.ungrandmarche.fr/boutiques/p/puericulture/hochet-arc-avec-anneau-et-grelot-rose-et-violet/1595306"),
        NewProductUgm(null, 1595582, "Collier de portage et d'allaitement ours bois brut, violet rose (max 74 cm)", "https://www.ungrandmarche.fr/boutiques/p/puericulture/collier-de-portage-et-d-allaitement-ours-bois-brut-violet-r/1595582"),
        NewProductUgm(null, 1594934, "Attache tétine silicone, perle koala, bleu/gris et blanc", "https://www.ungrandmarche.fr/boutiques/p/puericulture/attache-tetine-silicone-perle-koala-bleu-gris-et-blanc/1594934"))
        assertThat(products).containsExactlyInAnyOrderElementsOf(requiredProducts)
    }
}