package nl.rjcoding.common

enum class Direction(val angle: Int) {
    North(0),
    East(90),
    South(180),
    West(270);

    fun opposite(): Direction = when (this) {
        North -> South
        East -> West
        South -> North
        West -> East
    }

    companion object {
        fun fromAngle(angle: Int): Direction? = when (normalizeAngle(angle)) {
            0 -> North
            90 -> East
            180 -> South
            270 -> West
            else -> null
        }

        private tailrec fun normalizeAngle(angle: Int): Int = when {
            angle < 0 -> normalizeAngle(angle + 360)
            angle >= 360 -> normalizeAngle(angle % 360)
            else -> angle
        }
    }
}