package day13_1

import java.io.File

fun main() {
    val fileName = "src/day13/input.txt"
    val lines = File(fileName).readLines()

    val arcades = Arcade.fromText(lines)

    val sum = arcades.mapNotNull { findCost(it) }.sum()

    println(sum)


}

fun findCost(arcade: Arcade): Int? {
    val costs = mutableListOf<Solution>()
    for (a in 0 until 100) {
        for (b in 0 until 100) {
            val x = a * arcade.a.x + b * arcade.b.x
            val y = a * arcade.a.y + b * arcade.b.y
            if (arcade.prize == Prize(x, y)){
                costs.add(Solution(a, b))
            }
        }
    }
    return costs.minOfOrNull { it.cost() }
}

class Solution(val a: Int, val b: Int) {
    fun cost(): Int {
        return a * 3 + b * 1
    }
}

data class XY(val x: Int, val y: Int)

typealias Button = XY
typealias Prize = XY

class Arcade(val a: Button, val b: Button, val prize: Prize) {
    override fun toString(): String {
        return "Arcade(A=Button(x=${a.x}, y=${a.y}), B=Button(x=${b.x}, y=${b.y}), prize=Prize(x=${prize.x}, y=${prize.y}))"
    }

    companion object {
        fun fromText(lines: List<String>): List<Arcade> {
            return lines.windowed(4, 4, true).map { window ->
                val buttonA = parseButton(window[0])
                val buttonB = parseButton(window[1])
                val prize = parsePrize(window[2])

                Arcade(buttonA, buttonB, prize)
            }
        }

        private fun parseButton(line: String): Button {
            val regex = """Button [AB]: X\+(\d+), Y\+(\d+)""".toRegex()
            val matchResult = regex.find(line)!!
            val (x, y) = matchResult.destructured
            return Button(x.toInt(), y.toInt())
        }

        private fun parsePrize(line: String): Prize {
            val regex = """Prize: X=(\d+), Y=(\d+)""".toRegex()
            val matchResult = regex.find(line)!!
            val (x, y) = matchResult.destructured
            return Prize(x.toInt(), y.toInt())
        }
    }
}