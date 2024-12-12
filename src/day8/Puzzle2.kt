package day8_2

import java.io.File

fun main() {
    val fileName = "src/day8/example.txt"

    val lines = File(fileName).readLines()

    val area = Area(lines.size, lines[0].length)

    val antennas = readAntennas(lines)
    val antennaMap = antennas.groupBy { it.frequency }
    val antiNodes = antiNodes(antennaMap, area)
    val filtered = constrain(antiNodes, area)

    // filter out the nodes which are out of bounds
    println(filtered.size)
}

class Area(val height: Int, val width: Int){
    fun contains(position: Pos): Boolean{
        return position.x >= 0 && position.x < this.width && position.y >= 0 && position.y < this.height
    }
}

typealias Pos = Pair<Int, Int>

val Pos.x: Int
    get() = this.first

val Pos.y: Int
    get() = this.second

operator fun Pos.plus(other: Pos): Pos {
    return Pos(this.x + other.x, this.y + other.y)
}

operator fun Pos.minus(other: Pos): Pos {
    return Pos(this.x - other.x, this.y - other.y)
}

class Antenna(val frequency: Char, val position: Pos) {
    fun antiNodes(other: Antenna, area: Area): List<Pos> {
        val an: MutableList<Pos> = mutableListOf()
        val diff = other.position - this.position

        // move forward
        var next = other.position
        while(area.contains(next)){
            an.add(next)
            next += diff
        }

        // move back
        var previous = this.position
        while(area.contains(previous)){
            an.add(previous)
            previous -= diff
        }

        return an
    }
}

fun constrain(antiNodes: Set<Pos>, area: Area): List<Pos> {
    return antiNodes.filter { area.contains(it) }
}

fun antiNodes(antennaMap: Map<Char, List<Antenna>>, area: Area): Set<Pos> {
    val antiNodes: MutableSet<Pos> = mutableSetOf()
    antennaMap.forEach { (frequency, antennas) ->
        println("Processing frequency: $frequency")
        for (i in antennas.indices) {
            val left = antennas[i]
            for (j in i + 1..<antennas.size) {
                val right = antennas[j]
                val an = left.antiNodes(right, area)
                antiNodes.addAll(an)
            }
        }
    }
    return antiNodes
}

fun readAntennas(lines: List<String>): List<Antenna> {
    return lines.flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, char ->
            if (char != '.') Antenna(char, Pos(x, y)) else null
        }
    }
}