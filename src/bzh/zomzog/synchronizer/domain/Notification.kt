package bzh.zomzog.synchronizer.bzh.zomzog.synchronizer.domain


enum class ChangeType { CREATE, UPDATE, DELETE}

data class Notification<T>(val type: ChangeType, val id: Int, val entity: T)