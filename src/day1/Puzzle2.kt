package day1

import java.io.File

fun main() {
    val fileName = "src/day1/input.txt"
    val lines = File(fileName).readLines()
    val locs1 = mutableListOf<Int>()
    var locs2 = mutableListOf<Int>()
    lines.forEach {
        val (first, second) = it.split("\\s+".toRegex())
        locs1.add(first.toInt())
        locs2.add(second.toInt())
    }

    locs1.sort()
    locs2.sort()

    val locs2CountMap = locs2.groupingBy { it }.eachCount()

    var similarity = 0
    locs1.forEach { loc ->
        similarity += locs2CountMap.get(loc)?.times(loc) ?: 0
    }

    println(similarity)
}