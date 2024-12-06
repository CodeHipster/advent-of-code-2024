package day5_2

import java.io.File

fun main() {
    val fileName = "src/day5/input.txt"

    val lines = File(fileName).readLines()

    val split = lines.indexOf("")

    val rules = lines.slice(0..<split)
        .map { rule: String -> rule.split("|") }
        .map { rule: List<String> -> Rule(rule[0].toInt(), rule[1].toInt()) }
    val updates = lines.slice((split + 1)..<lines.size)
        .map { update -> update.split(",").map { s -> s.toInt() } }

    val sum = updates.filter { update -> rules.all { rule -> rule.check(update) } }
        .map {update -> update[update.size /2]}
        .sum()

    println(sum)
}

typealias Update = List<Int>

private class Rule(val before: Int, val after: Int) {

    fun check(update: Update): Boolean {
        val first = update.indexOf(before)
        val second = update.indexOf(after)
        if (first == -1 || second == -1) return true;
        return first < second
    }
}