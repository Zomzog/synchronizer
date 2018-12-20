package bzh.zomzog.synchronizer.repository

import bzh.zomzog.bzh.zomzog.synchronizer.domain.Ticker
import com.mongodb.async.client.MongoClient
import org.litote.kmongo.*
import org.litote.kmongo.async.getCollection
import org.litote.kmongo.coroutine.aggregate

class CoinRepository(client: MongoClient) {

    val collection = client.getDatabase("kotlin").getCollection<Ticker>()

    fun findBy(name: String) : Ticker {
        return collection.aggregate<Ticker>(
            match(Ticker::name eq name),
            sample(1)
        ).first()
    }
}