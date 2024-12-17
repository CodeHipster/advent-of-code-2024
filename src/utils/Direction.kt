package utils

sealed class Direction(val xy: XY) {
    data object Up : Direction(XY(0, -1))
    data object Down : Direction(XY(0, 1))
    data object Left : Direction(XY(-1, 0))
    data object Right : Direction(XY(1, 0))
}