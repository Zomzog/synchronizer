package bzh.zomzog.synchronizer.controller

import bzh.zomzog.synchronizer.domain.Product
import bzh.zomzog.synchronizer.service.ProductService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/products")
@ExperimentalCoroutinesApi
class ProductController(val productService: ProductService) {

    @FlowPreview
    @GetMapping @ResponseBody
    suspend fun list(): Flow<Product> {
        return productService.list()
    }

    @FlowPreview
    @GetMapping("/{id}") @ResponseBody
    suspend fun findOne(@PathVariable id: String): Product {
        return productService.findOne()
    }

}

