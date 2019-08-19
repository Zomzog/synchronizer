package bzh.zomzog.synchronizer

import bzh.zomzog.synchronizer.config.SynchronizerProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(SynchronizerProperties::class)
class SynchronizerApplication

fun main(args: Array<String>) {
	runApplication<SynchronizerApplication>(*args)
}
