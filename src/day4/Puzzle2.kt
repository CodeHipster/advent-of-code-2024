package day4_2

import java.io.File

fun main() {
    val fileName = "src/day4/input.txt"

    val lines = File(fileName).readLines()

    val grid = Grid.fromText(lines)

    val matchFunctions = listOf(::matchLT, ::matchRT)

    var count = 0;
    val match: (List<List<Char>>) -> Unit = { view ->
        if (matchFunctions.all { it(view) }) {
            count += 1
        }
    }

    grid.slide3x3(match)
    println(count)
}

// Match diagonal from left top
fun matchLT(view: List<List<Char>>): Boolean {
    return (view[0][0] == 'M' && view[1][1] == 'A' && view[2][2] == 'S') ||
            (view[0][0] == 'S' && view[1][1] == 'A' && view[2][2] == 'M')
}

// Match diagonal from right top
fun matchRT(view: List<List<Char>>): Boolean {
    return (view[0][2] == 'M' && view[1][1] == 'A' && view[2][0] == 'S') ||
            (view[0][2] == 'S' && view[1][1] == 'A' && view[2][0] == 'M')
}


private class Grid(val grid: List<List<Char>>) {

    companion object {
        fun fromText(lines: List<String>): Grid {
            val grid = lines.map { line -> line.toList() }
            return Grid(grid)
        }
    }

    fun slide3x3(operation: (List<List<Char>>) -> Unit) {
        grid.windowed(3, 1).flatMap { rows ->
            val s1 = rows[0].windowed(3, 1)
            val s2 = rows[1].windowed(3, 1)
            val s3 = rows[2].windowed(3, 1)
            s1.zip(s2).zip(s3) { (r1, r2), r3 -> listOf(r1, r2, r3) }
        }.forEach { view -> operation(view) }
    }
}