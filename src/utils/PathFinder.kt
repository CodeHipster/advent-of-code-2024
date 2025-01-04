package utils

import java.util.Stack

class PathFinder {
    fun findPath(start: Position, end: Position, grid: Grid<Char>): Int? {
        val visited = mutableMapOf<Position, Int>()
        val states = Stack<State>()

        fun next(state: State): List<State> {
            val neighbours = grid.neighbours(state.position)
            val nextSteps = state.steps + 1
            return neighbours
                .filter { n -> n.value != '#' }
                .filter { n ->
                    val prev = visited[n.position]
                    prev == null || prev > nextSteps // return as valid state if it is faster.
                }
                .map { n -> State(n.position, nextSteps) }
        }

        states.push(State(start, 0))

        var shortest: Int? = null

        while (states.isNotEmpty()) {
            val current = states.pop()
            visited[current.position] = current.steps

            if (current.position == end) {
                shortest = current.steps
            }

            states.addAll(next(current))
        }

        return shortest
    }


    data class State(val position: Position, val steps: Int)
}