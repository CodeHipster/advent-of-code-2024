package day5_1

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

    val sum = updates
        .filter { update -> rules.any { rule -> !rule.check(update) } }
        .map { update -> fixUpdate(update, rules) }
        .sumOf { update -> update[update.size / 2] }

    println(sum)
}

typealias Update = List<Int>

private fun fixUpdate(update: Update, rules: List<Rule>): Update {
    var newUpdate = update
    var correct = false
    outer@ while (!correct) {
        for (rule in rules) {
            if (!rule.check(newUpdate)) {
                newUpdate = rule.fix(newUpdate)
                continue@outer
            }
        }
        correct = true;
    }
    return newUpdate
}

private class Rule(val before: Int, val after: Int) {

    fun check(update: Update): Boolean {
        val first = update.indexOf(before)
        val second = update.indexOf(after)
        if (first == -1 || second == -1) return true;
        return first < second
    }

    fun fix(update: Update): Update {
        val mutable = update.toMutableList()
        val firstIndex = update.indexOf(before)
        val secondIndex = update.indexOf(after)
        mutable.move(firstIndex, secondIndex)
        return mutable.toList()
    }

}

fun <T> MutableList<T>.move(fromIndex: Int, toIndex: Int) {
    if (fromIndex !in indices || toIndex !in indices) {
        throw IndexOutOfBoundsException("Invalid indices")
    }
    val item = removeAt(fromIndex)
    add(toIndex, item)
}