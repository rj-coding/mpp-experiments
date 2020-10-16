package nl.rjcoding.common

import nl.rjcoding.jsinterop.UUIDAdapter

actual data class UUID(private val inner: String) {
    actual override fun toString(): String {
        return inner
    }

    actual override fun equals(other: Any?): Boolean {
        val res = inner.hashCode() == other.hashCode()
        return inner.hashCode() == other.hashCode()
    }

    actual override fun hashCode(): Int {
        return inner.hashCode()
    }

    actual companion object {
        actual fun random(): UUID {
            return UUID(UUIDAdapter.v4())
        }

        actual fun fromString(input: String): UUID? {
            if (UUIDAdapter.validate(input)) {
                return UUID(input)
            } else {
                return null
            }
        }
    }

}