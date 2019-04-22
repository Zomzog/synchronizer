package bzh.zomzog.synchronizer.controller

import bzh.zomzog.synchronizer.service.ProductService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class ProductController(val productService: ProductService){

    @GetMapping("/products")
    fun list() : Flux<String> {
        return Flux.empty()
    }
}

