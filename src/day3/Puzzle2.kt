package day3

import java.io.File
import java.util.Objects.nonNull

sealed class Operation {
    data class Multiply(val value: Int) : Operation()
    data object Enable : Operation()
    data object Disable : Operation()
}

fun main() {
    val fileName = "src/day3/input.txt"

    val pattern = Regex("(?<multiply>mul\\((?<val>\\d+),(?<mul>\\d+)\\))|(?<enable>do\\(\\))|(?<disable>don't\\(\\))")
    val lines = File(fileName).readLines()

    var sum = 0;
    var enabled = true;

    lines.flatMap { line -> pattern.findAll(line) }
        .map { match -> mapOperation(match) }
        .forEach { operation ->
            when (operation) {
                is Operation.Multiply -> if (enabled) sum += operation.value
                Operation.Disable -> enabled = false
                Operation.Enable -> enabled = true
            }
        }

    println(sum)
}

private fun mapOperation(match: MatchResult): Operation {
    if (nonNull(match.groups["multiply"])) {
        return Operation.Multiply(mul(match))
    }
    if (nonNull(match.groups["enable"])) {
        return Operation.Enable
    }
    if (nonNull(match.groups["disable"])) {
        return Operation.Disable
    }
    throw IllegalStateException("Should match any operation.")
}

private fun mul(match: MatchResult): Int {

    val value = match.groups["val"]?.value?.toInt() ?: throw IllegalStateException("val not valid")
    val mul = match.groups["mul"]?.value?.toInt() ?: throw IllegalStateException("mul not valid")

    val i = value * mul
//    println("value: $value, mul $mul, result $i")
    return i
}