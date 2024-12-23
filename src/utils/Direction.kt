package utils

typealias Up = Direction.Up
typealias Down = Direction.Down
typealias Left = Direction.Left
typealias Right = Direction.Right
typealias North = Direction.Up
typealias East = Direction.Right
typealias South = Direction.Down
typealias West = Direction.Left

sealed class Direction(val xy: XY) {
    data object Up : Direction(XY(0, -1))
    data object Down : Direction(XY(0, 1))
    data object Left : Direction(XY(-1, 0))
    data object Right : Direction(XY(1, 0))

    fun rotateRight():Direction{
        return when (this) {
            is Up -> Right
            is Right -> Down
            is Down -> Left
            is Left -> Up
        }
    }

    fun rotateLeft():Direction{
        return when (this) {
            is Up -> Left
            is Left -> Down
            is Down -> Right
            is Right -> Up
        }
    }

    fun reverse(): Direction{
        return when (this) {
            is Up -> Down
            is Left -> Right
            is Down -> Up
            is Right -> Left
        }
    }

    companion object {
        fun directions(): List<Direction> {
            return listOf(Up, Down, Left, Right)
        }
    }
}