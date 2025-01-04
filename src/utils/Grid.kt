package utils

import utils.Direction.Companion.directions

data class Grid<T>(val grid: List<MutableList<T>>) {
    data class Tile<T>(val value: T, val position: Position) {
        override fun toString(): String {
            return "(position=$position, value=$value)"
        }
    }

    fun neighbours(position: Position): List<Tile<T>> {
        return neighboursIndexed(position).map { p -> p.second }
    }

    fun neighboursIndexed(position: Position): List<Pair<Direction, Tile<T>>>{
        val neighbours = mutableListOf<Pair<Direction, Tile<T>>>()

        for (direction in directions()) {
            val newPosition = position + direction.xy

            if (contains(newPosition)) {
                val height = value(newPosition)
                neighbours.add(Pair(direction, Tile(height, newPosition)))
            }
        }

        return neighbours
    }

    fun value(position: Position): T {
        return grid[position.y][position.x]
    }

    fun contains(position: Position): Boolean {
        return position.y in grid.indices && position.x in grid[position.y].indices
    }

    fun find(c: T): List<Tile<T>> {
        return grid.foldIndexed(mutableListOf()) { y, accRow, row ->
            row.foldIndexed(accRow) { x, finds, value ->
                if (value == c) {
                    finds.add(Tile(c, Position(x, y)))
                }
                finds
            }
        }
    }

    @Override
    override fun toString(): String {
        return grid.joinToString("\n") { line -> line.joinToString("") }
    }

    companion object {
        fun fromText(text: List<String>): Grid<Char> {
            val tiles = text.map { row ->
                row.map{ char -> char }.toMutableList()
            }
            return Grid(tiles)
        }
    }
}