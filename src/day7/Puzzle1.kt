package day7_1

import java.io.File

fun main() {
    val fileName = "src/day7/input.txt"

    val lines = File(fileName).readLines()

    val tests = lines.map { Test.fromString(it) }

    val sum = tests.filter { solve(it) }.sumOf { it.result }

    println(sum)
}

private fun solve(test: Test): Boolean {
    val initialStates = listOf(
        State(test.numbers[0], test.numbers.drop(1)),
        State(test.numbers[0], test.numbers.drop(1))
    )

    val states = initialStates.sorted().toMutableList()

    while (states.isNotEmpty()) {
        val currentState = states.removeAt(0)

        if (currentState.left > test.result) continue

        if (currentState.right.isEmpty()) {
            if (currentState.left == test.result) {
                return true
            } else continue
        }

        val nextValue = currentState.right[0]
        val remainingValues = currentState.right.drop(1)

        val newStates = listOf(
            State(currentState.left + nextValue, remainingValues),
            State(currentState.left * nextValue, remainingValues),
            State(currentState.left concat nextValue, remainingValues),
        )

        states.addAll(newStates)
        states.sort()
    }
    return false
}

infix fun Long.concat(other: Long): Long {
    return (this.toString() + other.toString()).toLong()
}

class State(val left: Long, val right: List<Long>) : Comparable<State> {
    override fun compareTo(other: State): Int {
        return this.right.size.compareTo(other.right.size)
    }
}

class Test(val result: Long, val numbers: List<Long>) {
    companion object {
        fun fromString(input: String): Test {
            val parts = input.split(":")
            val result = parts[0].trim().toLong()
            val numbers = parts[1].trim().split(" ").map { it.toLong() }
            return Test(result, numbers)
        }
    }
}

sealed class Operation {
    data class Addition(val left: Int, val right: Int) : Operation()
    data class Multiplication(val left: Int, val right: Int) : Operation()
}