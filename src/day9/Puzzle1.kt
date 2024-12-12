package day9_1

import java.io.File
import java.lang.Math.addExact

fun main() {
    val fileName = "src/day9/input.txt"

    val line = File(fileName).readLines()[0].trim()

    val disk = line.flatMapIndexed { index, char -> unpack(char, index) }
    println(disk.asString())

    val defragmented = defragment(disk)
    println(defragmented.asString())

    val sum = defragmented
        .mapIndexedNotNull { index, c ->
            if (c !== null) index * c
            else null
        }.fold(0L) { acc, i ->
            addExact(acc, i)
        }

    println("$sum")
}

fun List<Long?>.asString(): String {
    return this.joinToString("") { it?.toString() ?: "." }
}

fun unpack(char: Char, index: Int): List<Long?> {
    val length = char.digitToInt()
    val value = if (index % 2 == 0) { // it is a file
        (index / 2).toLong()
    } else { // it is empty space
        null
    }
    return List(length) {value}
}

fun defragment(disk: List<Long?>): List<Long?> {
    val defragmented = disk.toMutableList()
    var start = 0
    var end = defragmented.size - 1

    while (start < end) {
        if (defragmented[start] == null) {
            if (defragmented[end] !== null) {
                defragmented[start] = defragmented[end]
                defragmented[end] = null
                end--
            } else { // end is not a digit, move one back.
                end--
                continue //
            }
        }
        start++
    }

    return defragmented
}