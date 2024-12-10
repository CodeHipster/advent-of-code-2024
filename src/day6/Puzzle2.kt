package day6_2

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

    guard.walk()
    val route = guard.tilesVisited
    route.removeIf { step -> step.first == guard.start }

    var loops: MutableSet<Pos> = mutableSetOf()
    for ((pos, _) in route) {
        val blockGrid = grid.addObstacle(pos)
        val loop = blockGrid.getGuard().walk()
        if (loop){
            loops.add(pos)
        }
    }

    println(loops.size)
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
                if (tile is Tile.START) Guard(x, y, this) else null
            }
        }.first()
    }

    fun addObstacle(pos: Pos): Grid {
        val grid = tiles.mapIndexed { y, line ->
            line.mapIndexed { x, tile ->
                if (pos.x == x && pos.y == y) Tile.BLOCKED
                else tile
            }
        }
        return Grid(grid)
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

private class Guard(var x: Int, var y: Int, val grid : Grid) {
    val start = Pos(x, y)
    var direction = Direction.UP
    var tilesVisited: MutableSet<Pair<Pos, Direction>> = mutableSetOf()

    enum class Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;

        fun turnRight(): Direction {
            return when (this) {
                UP -> RIGHT
                RIGHT -> DOWN
                DOWN -> LEFT
                LEFT -> UP
            }
        }
    }

    // return true if in a loop
    fun walk(): Boolean {
//        println("Starting at (${this.x},${this.y})")
        while (true) {
            val next = nextPos(Pos(x, y), this.direction)
            if (!isWithinBounds(next, grid)) {
                return false
            }
            if (blocked(next, grid)) {
//                println("Turning right")
                this.direction = this.direction.turnRight()
            } else {
                val added = tilesVisited.add(Pair(next, this.direction))
                if (!added) {
                    // Already passed this tile in same direction. Stuck in loop.

                    println("Detected Loop at: ${next}")
                    return true;
                }
                moveTo(next)
            }
        }
    }

    private fun isWithinBounds(pos: Pos, grid: Grid): Boolean {
        return pos.x in grid.tiles[0].indices && pos.y in grid.tiles.indices
    }

    private fun nextPos(pos: Pos, direction: Direction): Pos {
        return when (direction) {
            Direction.UP -> Pos(pos.x, pos.y - 1)
            Direction.DOWN -> Pos(pos.x, pos.y + 1)
            Direction.LEFT -> Pos(pos.x - 1, pos.y)
            Direction.RIGHT -> Pos(pos.x + 1, pos.y)
        }
    }

    private fun blocked(pos: Pos, grid: Grid): Boolean {
        return grid.tiles[pos.y][pos.x] == Tile.BLOCKED
    }

    private fun moveTo(pos: Pos) {
        x = pos.x
        y = pos.y
//        println("moving to: $pos, visited: ${this.tilesVisited.size}")
    }
}