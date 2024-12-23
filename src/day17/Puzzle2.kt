package day17_2

import java.io.File

fun main() {
    val fileName = "src/day17/input.txt"
    val lines = File(fileName).readLines()

    val (computer, program) = fromText(lines)

    val wanted = program.asIntList()

    for (i in 0 until 100000000){
        computer.registerA = i
        val result = computer.process(program)
        if (result == wanted){
            println(i)
            return
        }
        computer.reset()
    }
}

fun fromText(lines: List<String>): Pair<Computer, Program>{
    val registerA = 0
    val registerB = lines[1].substringAfter(": ").toInt()
    val registerC = lines[2].substringAfter(": ").toInt()

    val program = lines[4].substringAfter(": ")
        .split(",")
        .map { it.toInt() }
        .chunked(2)
        .map { Instruction(it[0], it[1]) }

    val computer = Computer(registerA, registerB, registerC)

    return Pair(computer, program)
}

fun Int.twoPow(): Int {
    return if (this >= 0) {
        1 shl this
    } else {
        throw IllegalArgumentException("Exponent must be non-negative")
    }
}

typealias Program = List<Instruction>
fun Program.asIntList(): List<Int> {
    return this.flatMap { instruction -> listOf(instruction.opCode, instruction.operant) }
}

data class Instruction(val opCode: Int, val operant: Int)

class Computer(var registerA: Int, var registerB: Int, var registerC: Int) {
    var output = mutableListOf<Int>()
    var programPointer = 0
    fun process(program: Program): List<Int> {
        while(programPointer < program.size){
            val instruction = program[programPointer]
            when(instruction.opCode){
                0 -> adv(instruction.operant)
                1 -> bxl(instruction.operant)
                2 -> bst(instruction.operant)
                3 -> jnz(instruction.operant)
                4 -> bxc()
                5 -> out(instruction.operant)
                6 -> bdv(instruction.operant)
                7 -> cdv(instruction.operant)
            }
            programPointer++
        }
        return output
    }

    fun reset(){
        output = mutableListOf()
        registerA = 0
        registerB = 0
        registerC = 0
        programPointer = 0
    }

    // The adv instruction (opcode 0) performs division. The numerator is the value in the A register.
    // The denominator is found by raising 2 to the power of the instruction's combo operand.
    // (So, an operand of 2 would divide A by 4 (2^2); an operand of 5 would divide A by 2^B.)
    // The result of the division operation is truncated to an integer and then written to the A register.
    private fun adv(operand: Int) {
        val op = combo(operand)
        registerA /= op.twoPow()
    }

    // The bxl instruction (opcode 1) calculates the bitwise XOR of register B and the instruction's literal operand,
    // then stores the result in register B.
    private fun bxl(operand: Int) {
        registerB = registerB.xor(operand)
    }

    // The bst instruction (opcode 2) calculates the value of its combo operand modulo 8
    // (thereby keeping only its lowest 3 bits), then writes that value to the B register.
    private fun bst(operand: Int) {
        val mask = 0x7
        registerB = (combo(operand) % 8).and(mask)
    }

    // The jnz instruction (opcode 3) does nothing if the A register is 0.
    // However, if the A register is not zero, it jumps by setting the instruction pointer to the value of its literal operand;
    // if this instruction jumps, the instruction pointer is not increased by 2 after this instruction.
    private fun jnz(operand: Int) {
        if (registerA == 0) return
        programPointer = (operand / 2) - 1
    }

    // The bxc instruction (opcode 4) calculates the bitwise XOR of register B and register C,
    // then stores the result in register B. (For legacy reasons, this instruction reads an operand but ignores it.)
    private fun bxc() {
        registerB = registerB.xor(registerC)
    }

    // The out instruction (opcode 5) calculates the value of its combo operand modulo 8, then outputs that value.
    // (If a program outputs multiple values, they are separated by commas.)
    private fun out(operand: Int) {
        val i = combo(operand) % 8
        output.add(i)
    }

    //The bdv instruction (opcode 6) works exactly like the adv instruction except that the result is stored in the B register.
    // (The numerator is still read from the A register.)
    private fun bdv(operand: Int) {
        val op = combo(operand)
        registerB = registerA / op.twoPow()
    }

    //The cdv instruction (opcode 7) works exactly like the adv instruction except that the result is stored in the C register.
    // (The numerator is still read from the A register.)
    private fun cdv(operand: Int) {
        val op = combo(operand)
        registerC = registerA / op.twoPow()
    }

    private fun combo(operand: Int): Int {
        return when (operand) {
            in 0..3 -> operand
            4 -> registerA
            5 -> registerB
            6 -> registerC
            else -> throw IllegalArgumentException("Invalid operand: $operand")
        }
    }
}