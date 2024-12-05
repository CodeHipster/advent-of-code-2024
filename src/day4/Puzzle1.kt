package day4

import java.io.File

val XMAS = "XMAS".toList()

fun main() {
    val fileName = "src/day4/input.txt"

    val lines = File(fileName).readLines()

    val grid = Grid.fromText(lines)

    println("horizontal")
    val horizontal = countLines(grid.horizontal())
    println("vertical")
    val vertical = countLines(grid.vertical())
    println("diagonal LT")
    val diagonalLT = countLines( grid.diagonalTL())
    println("diagonal RT")
    val diagonalRT = countLines( grid.diagonalTR())
    val sum = horizontal + vertical + diagonalLT + diagonalRT
    println(sum)
}

private fun countLine(line: List<Char>): Int{
    val count = line.windowed(4, 1, true).count { window -> XMAS == window }
//    if(count != 0) println("$line $count")
    return count
}

private fun countLines(lines: List<List<Char>>): Int{
    return lines.map { line -> countLine(line) + countLine(line.reversed()) }.sum()
}

private class Grid(val grid: List<List<Char>>) {

    fun horizontal(): List<List<Char>> {
        return grid;
    }

    fun vertical(): List<List<Char>> {
        val verticalGrid = MutableList(grid[0].size) { MutableList(grid.size) { Char.MIN_VALUE } }
        grid.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, value ->
                verticalGrid[colIndex][rowIndex] = value
            }
        }
        return verticalGrid;
    }

    fun diagonalTL(): List<List<Char>> {
        val diagonals = mutableListOf<List<Char>>()

        // Get diagonals from top-left to bottom-right
        for (k in 0..<grid.size + grid[0].size - 1) {
            val diagonal = mutableListOf<Char>()
            for (j in 0..k) {
                val i = k - j
                if (i < grid.size && j < grid[0].size) {
                    diagonal.add(grid[i][j])
                }
            }
            diagonals.add(diagonal)
        }
        return diagonals
    }

    fun diagonalTR(): List<List<Char>> {
        val diagonals = mutableListOf<List<Char>>()

        for (k in 0..<grid.size + grid[0].size - 1) {
            val diagonal = mutableListOf<Char>()
            for (j in 0..k) {
                val i = k - j
                if (i < grid.size && j < grid[0].size) {
                    diagonal.add(grid[i][grid[0].size - 1 - j])
                }
            }
            diagonals.add(diagonal)
        }
        return diagonals
    }

    companion object {
        fun fromText(lines: List<String>): Grid {
            val grid = lines.map { line -> line.toList() }
            return Grid(grid)
        }
    }
}