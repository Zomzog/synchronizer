package bzh.zomzog.synchronizer.service

import bzh.zomzog.synchronizer.domain.Product
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class ProductService {
    fun list(): Flux<Product> {
        TODO()
    }

}
