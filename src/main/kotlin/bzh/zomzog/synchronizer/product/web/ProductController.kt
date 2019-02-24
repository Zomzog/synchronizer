package bzh.zomzog.synchronizer.product.web

import bzh.zomzog.synchronizer.product.domain.NewProduct
import bzh.zomzog.synchronizer.product.repository.ProductRepository
import bzh.zomzog.synchronizer.product.service.ProductService
import bzh.zomzog.synchronizer.web.KodeinController
import bzh.zomzog.synchronizer.web.TypedRoute
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Location
import io.ktor.locations.delete
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.locations.put
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class ProductController(kodein: Kodein) : KodeinController(kodein) {

    val productRepository: ProductRepository by instance()

    val productService: ProductService by instance()

    override fun Routing.registerRoutes() {

        get<ProductController.Routes.Products> {
            call.respond(productRepository.getAll())
        }

        post<ProductController.Routes.Products> {
            val Product = call.receive<NewProduct>()
            call.respond(HttpStatusCode.Created, productRepository.add(Product))
        }

        put<ProductController.Routes.Products> {
            val product = call.receive<NewProduct>()
            val updated = productRepository.update(product)
            if (updated == null) call.respond(HttpStatusCode.NotFound)
            else call.respond(HttpStatusCode.OK, updated)
        }

        get<ProductController.Routes.Product> {
            val Product = productRepository.getOne(call.parameters["id"]?.toInt()!!)
            if (Product == null) call.respond(HttpStatusCode.NotFound)
            else call.respond(Product)
        }

        delete<ProductController.Routes.Product> {
            val removed = productRepository.delete(call.parameters["id"]?.toInt()!!)
            if (removed) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound)
        }

        get<Routes.ProductsDiff> {
            call.respond(productService.diff())
        }
    }

    /**
     * A class containing routes annotated with [Location] and implementing [TypedRoute].
     */
    object Routes {
        /**
         * Route for listing users.
         */
        @Location("/products")
        class Products : TypedRoute

        /**
         * Route for showing a specific Product from its [id].
         */
        @Location("/products/{id}")
        data class Product(val id: String) : TypedRoute

        /**
         * Route for listing users.
         */
        @Location("/products/diff")
        class ProductsDiff : TypedRoute
    }
}