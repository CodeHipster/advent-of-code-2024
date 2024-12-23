package day16_1

import utils.Direction
import utils.East
import utils.Grid
import utils.Position
import java.io.File
import java.util.*

fun main() {
    val fileName = "src/day16/input.txt"
    val lines = File(fileName).readLines()

    val maze = Maze.fromText(lines)

    val lowestScore = MazeSolver(maze).solve()

    println(lowestScore)
}

data class State(val position: Position, val direction: Direction, val score: Int)

class MazeSolver(val maze: Maze) {
    fun solve(): Int {
        val states = Stack<State>()
        val visited = mutableMapOf<Pair<Position, Direction>, Int>()
        var solution: Int? = null

        states.push(State(maze.start, East, 0))

        while (states.isNotEmpty()) {
            val current = states.pop()

            if (current.position == maze.end) {
                solution = solution?.let { minOf(it, current.score) } ?: current.score
                continue
            }
            visited[Pair(current.position, current.direction)] = current.score

            val nextStates = next(current).filter { state ->
                val v = visited[Pair(state.position, state.direction)]
                v == null || v > state.score
            }

            states.addAll(nextStates)
        }
        return solution!!
    }

    private fun next(state: State): List<State> {
        return maze.grid.neighboursIndexed(state.position).filter { (_, tile) -> tile.value == '.' }
            .map { (direction, tile) ->
                when (direction) {
                    state.direction -> {
                        State(tile.position, direction, state.score + 1)
                    }

                    state.direction.rotateRight(), state.direction.rotateLeft() -> {
                        State(tile.position, direction, state.score + 1001)
                    }

                    else -> {
                        State(tile.position, direction, state.score + 2001)
                    }
                }
            }
    }
}

class Maze(val grid: Grid<Char>, val start: Position, val end: Position) {

    companion object {
        fun fromText(text: List<String>): Maze {
            var start: Position? = null
            var end: Position? = null
            val tiles = text.mapIndexed { y, row ->
                row.mapIndexed { x, char ->
                    when (char) {
                        'S' -> {
                            start = Position(x, y); '.'
                        }

                        'E' -> {
                            end = Position(x, y); '.'
                        }

                        else -> char
                    }
                }.toMutableList()
            }
            return Maze(Grid(tiles), start!!, end!!)
        }
    }
}