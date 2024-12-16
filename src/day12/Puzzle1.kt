package day12_1

import java.io.File
import java.util.*

fun main() {
    val fileName = "src/day12/input.txt"
    val lines = File(fileName).readLines()
    val grid = lines.map { it.toCharArray() }.toTypedArray()
    val regionFinder = RegionFinder(grid)
    val regions = regionFinder.find()
    println("Number of regions: ${regions.size}")
    regions.forEachIndexed { index, region ->
        println("Region $index: $region")
    }

    val calculator = CostCalculator()

    val total = regions.map { calculator.calculate(it) }.sum()
    println(total)
}

data class Position(val x: Int, val y: Int){
    operator fun plus(other: Position): Position {
        return Position(this.x + other.x, this.y + other.y)
    }
    override fun toString(): String {
        return "(x=$x, y=$y)"
    }
}

data class Plot(val type: Char, val pos: Position){
    override fun toString(): String {
        return "(t=$type, p=$pos)"
    }
}

typealias Region = Set<Plot>

class CostCalculator{
    fun calculate(region: Region): Int{
        // define perimiter as 0
        // for each Plot as current in Region
        // for each other Plot in region
        // check if other plot is neighbour of current
        // add 1 to neighbour count
        // after comparing to others add 4 - neighbour count to the perimeter
        // sum up all plot perimeters
        // return perimeter * region size
        var perimeter = 0

        for (current in region) {
            var neighbourCount = 0

            for (other in region) {
                if (isNeighbour(current, other)) {
                    neighbourCount++
                }
            }

            perimeter += 4 - neighbourCount
        }
        println("Perimeter: $perimeter")

        return perimeter * region.size
    }

    private fun isNeighbour(plot1: Plot, plot2: Plot): Boolean {
        val dx = Math.abs(plot1.pos.x - plot2.pos.x)
        val dy = Math.abs(plot1.pos.y - plot2.pos.y)
        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1)
    }
}

class RegionFinder(private val grid: Array<CharArray>) {
    private val visited = Array(grid.size) { BooleanArray(grid[0].size) }
    private val directions = listOf(Position(0, 1), Position(1, 0), Position(0, -1), Position(-1, 0))

    fun find(): List<Region> {
        val regions = mutableListOf<Set<Plot>>()

        for (y in grid.indices) {
            for (x in grid[y].indices) {
                if (!visited[y][x]) {
                    val plot = Plot(grid[y][x], Position(x, y))
                    val region = search(plot)
                    if (region.isNotEmpty()) {
                        regions.add(region)
                    }
                }
            }
        }
        return regions
    }

    private fun search(start: Plot): Region{

        val region = mutableSetOf<Plot>()
        val stack = Stack<Position>()
        val type = start.type
        stack.push(start.pos)

        while (stack.isNotEmpty()) {
            val current  = stack.pop()
            val (x, y) = current
            if (x !in grid.indices || y !in grid[0].indices
                || visited[y][x]
                || grid[y][x] != type) {
                continue
            }
            visited[y][x] = true
            region.add(Plot(grid[y][x], Position(x, y)))

            for (dir in directions) {
                stack.push(current + dir)
            }
        }
        return region.toSet()
    }
}