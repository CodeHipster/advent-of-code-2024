package day9_1

import java.io.File
import java.lang.Math.addExact


// 88881734731 is incorrect

fun main() {
    val fileName = "src/day9/input.txt"

    val line = File(fileName).readLines()[0].trim()

    val unpacked = line.mapIndexed { index, char -> unpack(char, index) }.joinToString("")
    println(unpacked.length)

    val defragmented = defragment(unpacked)
    println(defragmented.length)

    val sum = defragmented
        .mapIndexedNotNull { index, c ->
            if (c.isDigit()) index * c.digitToInt()
            else null
        }.fold(0L) { acc, i ->
            addExact(acc, i.toLong())
        }

    println("$sum")
}

fun unpack(char: Char, index: Int): String {
    val length = char.digitToInt()
    if (index % 2 == 0) { // it is a file
        val fileNr = index / 2
        return fileNr.toString().repeat(length)
    } else { // it is empty space
        return ".".repeat(length)
    }
}

fun defragment(disk: String): String {
    val chars = disk.toCharArray()
    var start = 0
    var end = chars.size - 1

    while (start < end) {
        if (chars[start] == '.') {
            if (chars[end].isDigit()) {
                chars[start] = chars[end]
                chars[end] = '.'
                end--
            } else { // end is not a digit, move one back.
                end--
                continue //
            }
        }
        start++
    }

    return String(chars)
}