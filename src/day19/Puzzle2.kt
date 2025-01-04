package day19_2

import java.io.File
import java.util.*

// for each pattern we want to build. filter if pattern can be made.
// start with State(empty list of towels, full pattern)
// while there is states
// pop the first
// if the pattern is empty, return true
// for all towels, check if the characters match the start of the full pattern
// for those that match, add towel to state and remove from the pattern
// add all the states to the state list
// if states is empty, return false.

fun main() {
    val fileName = "src/day19/input.txt"
    val lines = File(fileName).readLines()

    val towels = lines[0].split(',').map { it.trim() } // Replace with actual towels
    val patterns = lines.subList(2, lines.size) // Replace with actual pattern


    val cache = mutableMapOf<String, Long>()

    fun canBuildPattern(pattern: String): Long {
        var count = 0L
        if (pattern == "") {
            return 1
        }

        val c = cache[pattern]
        if (c != null) return c

        for (towel in towels) {
            if (pattern.startsWith(towel)) {
                val newPattern = pattern.removePrefix(towel)
                val canBuildPattern = canBuildPattern(newPattern)
                cache[newPattern] = canBuildPattern
                count += canBuildPattern
            }
        }
        return count
    }

    val count = patterns.map {
        val cnt = canBuildPattern(it)
        println("towel: $it, count: $cnt")
        cnt
    }.sum()
    println(count)
}

data class State(val towels: List<String>, val made: String, val pattern: String)


fun canBuildPattern(towels: List<String>, pattern: String): Boolean {
    val cache = mutableMapOf<String, Boolean>()
    val initialState = State(emptyList(), "", pattern)
    val states = Stack<State>()
    states.push(initialState)

    var statesTried = 0

    while (states.isNotEmpty()) {
        val currentState = states.removeFirst()
        statesTried += 1
//        println(statesTried)
        // add to patterns we can make
        cache[currentState.made] = true

        // check cache for solution
        val c = cache[currentState.pattern]
        if (c != null) return c

        if (currentState.pattern.isEmpty()) {
            println("can build $pattern")
            return true
        }

        for (towel in towels) {
            if (currentState.pattern.startsWith(towel)) {
                val newTowels = currentState.towels + towel
                val newPattern = currentState.pattern.removePrefix(towel)
                val made = currentState.made + towel
                states.push(State(newTowels, made, newPattern))
            } else {
                // can't make pattern
                // add to patterns we cant make, including towel.
                cache[currentState.made + towel] = false
            }
        }
    }

    println("can not build $pattern")
    return false
}