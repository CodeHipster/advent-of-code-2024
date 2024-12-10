package day6_1

import java.io.File

typealias Pos = Pair<Int, Int>

val Pos.x: Int
    get() = this.first

val Pos.y: Int
    get() = this.second

fun main() {
    val fileName = "src/day6/input.txt"

    val lines = File(fileName).readLines()

    val grid = Grid.fromText(lines)
    val guard = grid.getGuard()

    guard.walk(grid)

    val steps = guard.tilesVisited.size

    println(steps)
}


sealed class Tile {
    data object BLOCKED : Tile()
    data object OPEN : Tile()
    data class START(val x: Int, val y: Int) : Tile()
}

private class Grid(val tiles: List<List<Tile>>) {
    fun getGuard(): Guard {
        return tiles.flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, tile ->
                if (tile is Tile.START) Guard(x, y) else null
            }
        }.first()
    }

    companion object {
        fun fromText(text: List<String>): Grid {
            val grid = text.mapIndexed { y, line ->
                line.mapIndexed { x, char ->
                    when (char) {
                        '#' -> Tile.BLOCKED
                        '.' -> Tile.OPEN
                        '^' -> Tile.START(x, y)
                        else -> throw IllegalStateException("unexpected char")
                    }
                }
            }
            return Grid(grid)
        }
    }
}

private class Guard(var x: Int, var y: Int) {
    var direction = Direction.UP
    var tilesVisited: MutableSet<Pos> = mutableSetOf()

    enum class Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    fun walk(grid: Grid) {
        println("Starting at (${this.x},${this.y})")
        while (true) {
            val next = nextPos()
            if (!isWithinBounds(next, grid)) {
                return
            }
            if (blocked(next, grid)) {
                println("Turning right")
                turnRight()
            } else {
                tilesVisited.add(next)
                moveTo(next)
            }
        }
    }

    private fun isWithinBounds(pos: Pos, grid: Grid): Boolean {
        return pos.x in grid.tiles[0].indices && pos.y in grid.tiles.indices
    }

    private fun nextPos(): Pos {
        return when (direction) {
            Direction.UP -> Pos(x, y - 1)
            Direction.DOWN -> Pos(x, y + 1)
            Direction.LEFT -> Pos(x - 1, y)
            Direction.RIGHT -> Pos(x + 1, y)
        }
    }

    private fun blocked(pos: Pos, grid: Grid): Boolean {
        return grid.tiles[pos.y][pos.x] == Tile.BLOCKED
    }

    private fun moveTo(pos: Pos) {
        x = pos.x
        y = pos.y
        println("moving to: $pos, visited: ${this.tilesVisited.size}")
    }

    private fun turnRight() {
        direction = when (direction) {
            Direction.UP -> Direction.RIGHT
            Direction.RIGHT -> Direction.DOWN
            Direction.DOWN -> Direction.LEFT
            Direction.LEFT -> Direction.UP
        }
    }
}