package day3

import java.io.File

fun main() {
    val fileName = "src/day3/input.txt"

    val pattern = Regex("mul\\((?<val>\\d+),(?<mul>\\d+)\\)")
    val lines = File(fileName).readLines()

    val sum = lines.flatMap { line -> pattern.findAll(line) }.sumOf { match ->
        mul(match)
    }

    println(sum)
}

private fun mul(match: MatchResult): Int {

    val value = match.groups["val"]?.value?.toInt() ?: throw IllegalStateException("val not valid")
    val mul = match.groups["mul"]?.value?.toInt() ?: throw IllegalStateException("mul not valid")

    val i = value * mul
//    println("value: $value, mul $mul, result $i")
    return i
}