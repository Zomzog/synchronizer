package bzh.zomzog.synchronizer.controller

import bzh.zomzog.synchronizer.domain.Product
import bzh.zomzog.synchronizer.service.ProductService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux

@WithMockUser
@WebFluxTest(ProductController::class)
internal class ProductControllerTest(@Autowired val client: WebTestClient) {

    @MockkBean
    private lateinit var productService: ProductService

    @Test
    fun `List all products`() {
        every { productService.list() }.returns(Flux.just(Product("product1"), Product("product2"), Product("product3")))
        client.get().uri("/products").exchange()
            .expectStatus().is2xxSuccessful
            .expectBodyList(Product::class.java)
            .hasSize(3)
    }

}