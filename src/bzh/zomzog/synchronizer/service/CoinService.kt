package bzh.zomzog.synchronizer.service

import bzh.zomzog.bzh.zomzog.synchronizer.domain.Ticker
import bzh.zomzog.synchronizer.repository.CoinRepository
import java.time.Duration
import java.time.LocalDateTime

class CoinService(val repository: CoinRepository) {

    fun find(symbol: String, start: LocalDateTime, end: LocalDateTime) : List<Ticker> {
        if(Duration.between(end, start).toDays() > 31){
            throw IllegalStateException("Maximum number of days for full queries can not exceed 31 calendar days")
        }
        return repository.findInRange(symbol, start, end)
    }

}