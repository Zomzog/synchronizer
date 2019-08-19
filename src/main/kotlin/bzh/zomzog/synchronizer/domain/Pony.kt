package bzh.zomzog.synchronizer.domain

data class Pony(val name: String,
                val name2: String,
                val name3: String,
                val name4: String,
                val name5: String,
                val name6: String,
                val name7: String,
                val name8: String) {

    override fun toString(): String {
        return "Pony(name='$name', name2='$name2', name3='$name3', name4='$name4', name5='$name5', name6='$name6', name7='$name7', name8='$name8')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Pony

        if (name != other.name) return false
        if (name2 != other.name2) return false
        if (name3 != other.name3) return false
        if (name4 != other.name4) return false
        if (name5 != other.name5) return false
        if (name6 != other.name6) return false
        if (name7 != other.name7) return false
        if (name8 != other.name8) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + name2.hashCode()
        result = 31 * result + name3.hashCode()
        result = 31 * result + name4.hashCode()
        result = 31 * result + name5.hashCode()
        result = 31 * result + name6.hashCode()
        result = 31 * result + name7.hashCode()
        result = 31 * result + name8.hashCode()
        return result
    }


}