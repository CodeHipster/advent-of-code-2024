package day18_2

import utils.Grid
import utils.PathFinder
import utils.Position
import java.io.File

fun main() {
    val fileName = "src/day18/input.txt"
    val lines = File(fileName).readLines()

    // read lines as coordinates of obstacles as #
    // create grid of Char of 6x6
    // add the obstacles as #

    val memory = Grid(List(71) { MutableList(71) { '.' } })
    val memDrops = readMemory(lines)
    val newMemory = memDrops.subList(0, 1024)
    dropMemory(newMemory, memory)

    val start = 1024
    val first = memDrops.subList(start, memDrops.size).mapIndexedNotNull { index, (x, y) ->
        memory.grid[y][x] = '#'
        val steps = PathFinder().findPath(Position(0, 0), Position(70, 70), memory)
        if (steps != null) {
            null
        } else {
            index + start
        }
    }.first()

    // find path through grid
    println(memDrops[first!!])
}

fun dropMemory(positions: List<Position>, memory: Grid<Char>) {
    positions.forEach { (x, y) -> memory.grid[y][x] = '#' }
}

fun readMemory(lines: List<String>): List<Position> {
    // Parse the lines to extract coordinates of obstacles
    return lines.map { line ->
        val (x, y) = line.split(",").map { it.trim().toInt() }
        Position(x, y)
    }
}