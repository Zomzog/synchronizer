package bzh.zomzog.synchronizer.controller

import assertk.assertions.containsAll
import assertk.assertions.isEqualTo
import bzh.zomzog.synchronizer.domain.Product
import bzh.zomzog.synchronizer.service.ProductService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flowOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.*
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.expectBodyList

@FlowPreview
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
internal class ProductControllerTest(@Autowired val client: WebTestClient) {

    @MockkBean
    private lateinit var productService: ProductService

    @Test
    fun `List all products`() {
        coEvery { productService.list() } returns (flowOf(Product("product1"), Product("product2"), Product("product3")))

        val result = client.get()
            .uri("/products")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBodyList(Product::class.java)
            .hasSize(3)
            .returnResult()

        assertThat(result.responseBody)
            .containsExactlyInAnyOrder(Product("product1"), Product("product2"), Product("product3"))
    }

    @Test
    fun `Find one by id`() {
        coEvery { productService.findOne() } returns (Product("product1"))

        val result = client.get()
            .uri("/products/myId")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(Product::class.java)
            .returnResult()

        assertThat(result.responseBody).isEqualTo(Product("product1"))
    }

}