package bzh.zomzog.synchronizer.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("synchronizer")
class SynchronizerProperties {

    lateinit var etsyApiKey: String

}