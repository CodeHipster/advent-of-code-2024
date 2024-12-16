package day14_2

import java.io.File
import java.lang.Thread.sleep

// 48 horizontal
// 126 vertical
// 149
// 229

// 332

fun main() {
    val fileName = "src/day14/input.txt"
    val lines = File(fileName).readLines()

    val room = Room(101, 103)
//    val room = Room(11, 7)
    val quadrants = Quadrant.fromRoom(room)

    var guards = lines
        .map { Guard.parseGuard(it) }

    // repeat at 10404
    var start1 = 48
    var start2 = 126
    var repeat1 = 101
    var repeat2 = 103
    var seconds = start2
    while (seconds < 10404) {
        val moved = guards.map { it.move(seconds, room) }
        room.printRoom(moved)
        println("seconds: $seconds")
        sleep(200)
        seconds += repeat2
    }
}

data class XY(val x: Int, val y: Int)
data class Quadrant(val lt: XY, val rb: XY) {
    fun contains(point: XY): Boolean {
        return point.x in lt.x..rb.x && point.y in lt.y..rb.y
    }

    companion object {
        fun fromRoom(room: Room): List<Quadrant> {
            val midX = room.x / 2
            val midY = room.y / 2

            val quadrant1 = Quadrant(XY(0, 0), XY(midX - 1, midY - 1))
            val quadrant2 = Quadrant(XY(midX + 1, 0), XY(room.x - 1, midY - 1))
            val quadrant3 = Quadrant(XY(0, midY + 1), XY(midX - 1, room.y - 1))
            val quadrant4 = Quadrant(XY(midX + 1, midY + 1), XY(room.x - 1, room.y - 1))

            return listOf(quadrant1, quadrant2, quadrant3, quadrant4)
        }
    }
}

typealias Position = XY
typealias Velocity = XY
typealias Room = XY

fun Room.printRoom(guards: List<Guard>) {
    val roomArray = Array(y) { CharArray(x) { ' ' } }

    for (guard in guards) {
        roomArray[guard.pos.y][guard.pos.x] = 'O'
    }
    for (row in roomArray) {
        println(row.joinToString(""))
    }
}

data class Guard(val pos: Position, val velocity: Velocity) {

    fun move(seconds: Int, room: Room): Guard {
        var newX = (pos.x + velocity.x * seconds) % room.x
        var newY = (pos.y + velocity.y * seconds) % room.y

        // If moving in negative direction, we might need to add the room
        if (newX < 0) newX += room.x
        if (newY < 0) newY += room.y

        return copy(pos = Position(newX, newY))
    }

    companion object {
        fun parseGuard(line: String): Guard {
            val regex = """p=(\d+),(\d+) v=(-?\d+),(-?\d+)""".toRegex()
            val matchResult = regex.find(line)!!
            val (px, py, vx, vy) = matchResult.destructured
            val position = Position(px.toInt(), py.toInt())
            val direction = Velocity(vx.toInt(), vy.toInt())
            return Guard(position, direction)
        }
    }
}


//p=0,4 v=3,-3
//p=6,3 v=-1,-3