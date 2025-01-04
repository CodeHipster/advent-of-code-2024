package day18_1

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
    val newMemory = readMemory(lines).subList(0, 1024)
    dropMemory(newMemory, memory)

    println(memory)

    // find path through grid
    val steps = PathFinder().findPath(Position(0, 0), Position(70, 70), memory)
    println(steps)
}

fun dropMemory(positions: List<Position>, memory: Grid<Char>){
    positions.forEach { (x, y) -> memory.grid[y][x] = '#'}
}

fun readMemory(lines: List<String>): List<Position> {
    // Parse the lines to extract coordinates of obstacles
    return lines.map {line ->
        val (x, y) = line.split(",").map { it.trim().toInt() }
        Position(x,y)
    }
}