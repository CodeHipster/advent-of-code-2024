package day2

import java.io.File
import kotlin.math.abs

fun main() {
    val fileName = "src/day2/input.txt"
    val lines = File(fileName).readLines()
    val count = lines.map { line -> line.split("\\s+".toRegex()).map { nr -> nr.toInt() } }
        .count { record ->
            combinations(record).any { variant ->
                inOrder(variant) && smooth(variant)
            }
        }
    println(count)
}

fun inOrder(record: List<Int>): Boolean = record.windowed(3, 1)
    .all { nrs -> (nrs[0] > nrs[1] && nrs[1] > nrs[2]) || (nrs[0] < nrs[1] && nrs[1] < nrs[2]) }

fun smooth(record: List<Int>): Boolean = record.windowed(2, 1)
    .all { nrs -> abs(nrs[0] - nrs[1]) <= 3 }

fun combinations(nrs: List<Int>): List<List<Int>> {
    return nrs.indices.map { index ->
        nrs.filterIndexed { i, _ -> i != index }
    }
}