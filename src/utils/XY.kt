package utils

data class XY(val x: Int, val y: Int){
    operator fun plus(other: XY): XY {
        return XY(this.x + other.x, this.y + other.y)
    }

    operator fun minus(other: XY): XY {
        return XY(this.x - other.x, this.y - other.y)
    }

    override fun toString(): String {
        return "(x=$x, y=$y)"
    }
}

typealias Position = XY

