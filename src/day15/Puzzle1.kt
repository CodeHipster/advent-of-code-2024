package day15_1

import utils.Direction
import utils.Position
import java.io.File

fun main() {
    val fileName = "src/day15/input.txt"
    val lines = File(fileName).readLines()
    val split = lines.mapIndexedNotNull { index, line -> if (line.isEmpty()) index else null }.first()
    val warehouse = Warehouse.fromText(lines.subList(0, split))
    val instructions = instructionSet(lines.subList(split, lines.size))

    warehouse.runRobot(instructions)

    println(warehouse)

    val sum = warehouse.boxes().map { (x, y) -> x + 100 * y }.sum()
    println(sum)
}

typealias Instruction = Direction
typealias InstructionSet = List<Instruction>

fun instructionSet(lines: List<String>): InstructionSet {
    return lines.joinToString("").map { c ->
        when (c) {
            '^' -> Direction.Up
            'v' -> Direction.Down
            '<' -> Direction.Left
            '>' -> Direction.Right
            else -> throw IllegalArgumentException("Unknown instruction $c")
        }
    }
}

sealed class Entity {

    data object Robot : Entity() {
        override fun toString(): String {
            return "@"
        }
    }

    data object Box : Entity() {

        override fun toString(): String {
            return "O"
        }
    }

    data object Wall : Entity() {

        override fun toString(): String {
            return "#"
        }
    }

    data object Space : Entity() {

        override fun toString(): String {
            return "."
        }
    }
}

class Warehouse(val entities: Array<Array<Entity>>) {
    companion object {
        fun fromText(lines: List<String>): Warehouse {
            val entities = Array(lines.size) { y ->
                Array(lines[y].length) { x ->
                    when (lines[y][x]) {
                        '.' -> Entity.Space
                        '@' -> Entity.Robot
                        'O' -> Entity.Box
                        '#' -> Entity.Wall
                        else -> throw IllegalArgumentException("Unknown entity ${lines[y][x]} at $x, $y")
                    }
                }
            }
            return Warehouse(entities)
        }
    }

    fun boxes(): List<Position> {
        return entities.flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, entity ->
                when (entity) {
                    is Entity.Box -> Position(x, y)
                    else -> null
                }
            }
        }
    }

    fun robot(): Position {
        entities.forEachIndexed { y, row ->
            row.forEachIndexed { x, entity ->
                if (entity is Entity.Robot) {
                    return Position(x, y)
                }
            }
        }
        throw IllegalStateException("Robot not found in the warehouse")
    }

    fun runRobot(instructions: InstructionSet) {
        var robotPosition = robot()

        instructions.forEach { instruction ->
            val canMove = canMove(robotPosition, instruction)
            if (canMove != null) {
                val swap = entities[canMove.y][canMove.x]
                move(swap, robotPosition, instruction)
                robotPosition += instruction.xy
            }
        }
    }

    private fun canMove(position: Position, direction: Direction): Position? {

        val newPosition = position + direction.xy
        return when (entities[newPosition.y][newPosition.x]) {
            is Entity.Space -> newPosition
            is Entity.Box -> canMove(newPosition, direction)
            else -> null
        }
    }

    private fun move(entity: Entity, from: Position, direction: Direction): Entity {
        val nextPos = from + direction.xy
        val swap = entities[from.y][from.x]
        entities[from.y][from.x] = entity

        return if (swap is Entity.Space) {
            swap
        } else {
            move(swap, nextPos, direction)
        }
    }

    @Override
    override fun toString(): String {
        return entities.joinToString("\n") { line -> line.joinToString("") }
    }
}
