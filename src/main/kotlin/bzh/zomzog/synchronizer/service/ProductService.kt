package bzh.zomzog.synchronizer.service

import bzh.zomzog.synchronizer.domain.Product
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@FlowPreview
@Service
class ProductService {
    suspend fun list(): Flow<Product> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun findOne(): Product {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
