package bzh.zomzog.bzh.zomzog.synchronizer.domain

import io.ktor.locations.Location
import java.time.LocalDate
import java.time.LocalDateTime

data class Ticker(val symbol: String, val name: String, val price: Double, val marketCap: Double, val lastUpdated: LocalDateTime)

@Location("/coins/{symbol}")
data class TickerRequest(val symbol: String, val start: LocalDate, val end: LocalDate)