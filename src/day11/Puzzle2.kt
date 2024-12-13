package day11_1

import java.io.File

fun main() {
    val fileName = "src/day11/input.txt"

    val lines = File(fileName).readLines()

    var stones = lines[0].split(" ").map { nr -> nr.toLong() }


    for (i in 0 until 25){
        stones = blink(stones)
        println("${i+1}: ${stones.size}")
    }

    println(stones.size)

}

typealias Stone = Long
typealias Stones = List<Stone>

fun blink(stones: Stones): Stones{
    val newStones = mutableListOf<Stone>()

    for (stone in stones){
        if(stone == 0L){
            newStones.add(1)
            continue
        }
        val splitStones = split(stone)
        if(splitStones != null){
            newStones.addAll(splitStones)
            continue
        }
        newStones.add(stone * 2024)
    }
    return newStones
}

fun split(stone: Stone): Stones?{
    val asString = stone.toString()
    val length = asString.length
    val even = (length % 2) == 0
    return if(even) {
//        println("Splitting even: $stone, $length")
        val mid = length / 2
        val firstHalf = asString.substring(0, mid).toLong()
        val secondHalf = asString.substring(mid).toLong()
        listOf(firstHalf, secondHalf)
    }else{
        null
    }
}
