package nl.rjcoding.common

expect class UUID {

    override fun toString(): String

    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int

    companion object {
        fun random(): UUID
        fun fromString(input: String): UUID?
    }
}