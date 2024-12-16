package day14_1

import java.io.File

//87476760 too low

fun main() {
    val fileName = "src/day14/input.txt"
    val lines = File(fileName).readLines()

    val room = Room(101, 103)
//    val room = Room(11, 7)
    val quadrants = Quadrant.fromRoom(room)

    val guards = lines
        .map { Guard.parseGuard(it) }
        .map { it.move(100, room) }

    val quadrantCounts = quadrants.associateWith { quadrant ->
        guards.count { guard -> quadrant.contains(guard.pos) }
    }

    val multiply = quadrantCounts.values.reduce { acc, i -> i * acc }
    println(multiply)
}

data class XY(val x: Int, val y: Int)
data class Quadrant(val lt: XY, val rb: XY){
    fun contains(point: XY): Boolean {
        return point.x in lt.x..rb.x && point.y in lt.y..rb.y
    }
    companion object{
        fun fromRoom(room: Room): List<Quadrant>{
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

data class Guard(val pos: Position, val velocity: Velocity){

    fun move(seconds: Int, room: Room): Guard {
        var newX = (pos.x + velocity.x * seconds) % room.x
        var newY = (pos.y + velocity.y * seconds) % room.y

        // If moving in negative direction, we might need to add the room
        if (newX < 0) newX += room.x
        if (newY < 0) newY += room.y

        return copy(pos = Position(newX, newY))
    }

    companion object{
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