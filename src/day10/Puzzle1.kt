package day10_1

import day10_1.Direction.Companion.directions
import java.io.File
import java.util.*

fun main() {
    val fileName = "src/day10/input.txt"

    val lines = File(fileName).readLines()

    val grid = Grid.fromText(lines)

    val trailHeads = grid.trailHeads()
    // for each trailHead in grid
    val sum = trailHeads.map { head ->
        val tile = Tile(grid.height(head), head)
        findTops(grid, tile)
    }.sum()
    println(sum)
}

fun findTops(grid: Grid, trailHead: Tile): Int {
    val states = Stack<Tile>()
    val tops = mutableSetOf<Position>()
    val visited = mutableSetOf<Tile>()

    states.push(trailHead)
    println("Starting at $trailHead")

    while (states.isNotEmpty()) {
        val current = states.pop()
        println("Moved to: $current")

        if (current.height == 9) {
            println("Found top!")
            tops.add(current.position)
            continue
        }

        val neighbours = grid.neighbours(current.position)
            .filter { tile -> tile.height == current.height + 1 }
            .filter { tile -> !visited.contains(tile) }

        states.addAll(neighbours)
        visited.addAll(neighbours)
    }
    // walk all paths depth first
    // if position already visited, abandon
    // add visited positions to a set of visited
    // if position is a top add it to the set of tops
    // add valid neighbours to ordered state set
    println("Tops found: $tops")
    return tops.size
}

sealed class Direction(x: Int, y: Int) : Position(x, y) {
    data object Up : Direction(0, -1)
    data object Down : Direction(0, 1)
    data object Left : Direction(-1, 0)
    data object Right : Direction(1, 0)

    companion object {
        fun directions(): List<Direction> {
            return listOf(Up, Down, Left, Right)
        }
    }
}

fun String.beepboop(): String {
    return this.replace("e", "beep").replace("o", "boop")
}


open class Position(val x: Int, val y: Int) {
    operator fun plus(other: Position): Position {
        return Position(this.x + other.x, this.y + other.y)
    }

    override fun toString(): String {
        return "(x=$x, y=$y)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Position) return false
        return x == other.x && y == other.y
    }

    override fun hashCode(): Int {
        return 31 * x + y
    }
}

class Tile(val height: Int, val position: Position) {
    override fun toString(): String {
        return "(position=$position, height=$height)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Tile) return false
        return height == other.height && position == other.position
    }

    override fun hashCode(): Int {
        return 31 * height + position.hashCode()
    }
}

class Grid(val grid: List<List<Int>>) {
    fun neighbours(position: Position): List<Tile> {
        val neighbours = mutableListOf<Tile>()

        for (direction in directions()) {
            val newPosition = position + direction

            if (contains(newPosition)) {
                val height = height(newPosition)
                neighbours.add(Tile(height, newPosition))
            }
        }

        return neighbours
    }

    fun height(position: Position): Int {
        return grid[position.y][position.x]
    }

    fun contains(position: Position): Boolean {
        return position.y in grid.indices && position.x in grid[position.y].indices
    }

    fun trailHeads(): List<Position> {
        return grid.foldIndexed(mutableListOf()) { y, accRow, row ->
            row.foldIndexed(accRow) { x, heads, value ->
                if (value == 0) {
                    heads.add(Position(x, y))
                }
                heads
            }
        }
    }

    companion object {
        fun fromText(text: List<String>): Grid {
            val tiles = text.mapIndexed { y, row ->
                row.mapIndexed { x, char -> char.digitToInt() }
            }
            return Grid(tiles)
        }
    }

}
