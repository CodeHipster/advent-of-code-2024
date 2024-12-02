package day1

import java.io.File
import kotlin.math.abs

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

    val totalDiff = locs1.zip(locs2).map { (first, second) -> abs(first - second) }.sum()

    println(totalDiff)
}