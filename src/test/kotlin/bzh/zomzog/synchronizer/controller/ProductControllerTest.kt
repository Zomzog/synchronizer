package bzh.zomzog.synchronizer.controller

import bzh.zomzog.synchronizer.service.ProductService
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.reactive.server.WebTestClient

@WithMockUser
@WebFluxTest(ProductController::class)
internal class ProductControllerTest(@Autowired val client: WebTestClient) {

    @MockkBean
    private lateinit var productService: ProductService

    @Test
    fun `List all products`() {
        client.get().uri("/products").exchange()
            .expectStatus().is2xxSuccessful
    }

}