package day11_1

import java.io.File

fun main() {
    val fileName = "src/day11/input.txt"

    val lines = File(fileName).readLines()

    val stones = lines[0].split(" ").map { nr -> nr.toLong() }

    val cache = BlinkCache()
    var sum = 0L;
    for (stone in stones){
        sum += cache.blink75(stone, 0)
    }

    println(sum)
}

// Using some sort of memoization with recursion. Not the cleanest code ':)
// But very fast
class BlinkCache {
    val cache = mutableMapOf<Pair<Stone, Int>, Long>()
    fun blink75(stone: Stone, depth: Int): Long {
        val key = Pair(stone, depth)
        val cachedCount = cache[key]
        if (cachedCount == null) {
            if (depth == 75) {
                return 1
            }
            if (stone == 0L) {
                val count = blink75(1, depth + 1)
                cache[key] = count
                return count
            }
            val splitStones = split(stone)
            if (splitStones != null) {
                val count = blink75(splitStones[0], depth + 1) + blink75(splitStones[1], depth + 1)
                cache[key] = count
                return count
            }
            val count = blink75(stone * 2024, depth + 1)
            cache[key] = count
            return count
        }else{
            return cachedCount
        }
    }
}

typealias Stone = Long
typealias Stones = List<Stone>

fun split(stone: Stone): Stones? {
    val asString = stone.toString()
    val length = asString.length
    val even = (length % 2) == 0
    return if (even) {
        val mid = length / 2
        val firstHalf = asString.substring(0, mid).toLong()
        val secondHalf = asString.substring(mid).toLong()
        listOf(firstHalf, secondHalf)
    } else {
        null
    }
}
