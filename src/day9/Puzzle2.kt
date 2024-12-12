package day9_2

import java.io.File
import java.lang.Math.addExact

fun main() {
    val fileName = "src/day9/input.txt"

    val line = File(fileName).readLines()[0].trim()

    val disk = unpack(line)
    println(disk.asString())

    val defragmented = defragment(disk)
    println(defragmented.asString())

    val sum = checksum(defragmented)

    println("$sum")
}

fun checksum(disk: Disk): Long {
    return disk.asBlocks()
        .mapIndexedNotNull { index, c ->
            if (c !== null) index * c
            else null
        }.fold(0L) { acc, i ->
            addExact(acc, i.toLong())
        }
}

fun unpack(compressed: Compressed): Disk {

    val disk = compressed.mapIndexed { index, char ->
        val length = char.digitToInt()
        val value = if (index % 2 == 0) { // it is a file
            val fileNr = (index / 2)
            BlockGroup.File(length, fileNr)
        } else { // it is empty space
            BlockGroup.Empty(length)
        }
        value
    }
    return disk
}

fun defragment(disk: Disk): Disk {
    val defragmented = disk.toMutableList()

    // iterate blocks from right to left
    // if block is a file
    // copy the file
    // iterate over left
    // find empty block that can hold file
    // insert file in front of empty space
    // reduce empty space with size of file
    // replace original file with empty space.
    var index = defragmented.lastIndex
    while (index > 0) {
        val block = defragmented[index]
        if (block is BlockGroup.File) {
            for (j in 0 until index) {
                val emptyBlock = defragmented[j]
                if (emptyBlock is BlockGroup.Empty && emptyBlock.size >= block.size) {
                    defragmented[j] = BlockGroup.Empty((emptyBlock.size - block.size))
                    defragmented.add(j, block)
                    index++
                    defragmented[index] = BlockGroup.Empty(block.size)
                    break
                }
            }
        }
        index--
    }

    return defragmented
}

typealias Compressed = String
typealias Block = Int?
typealias Disk = List<BlockGroup>

fun Disk.asString(): String {
    return this.joinToString("") { it.toString() }
}

fun Disk.asBlocks(): List<Int?> {
    return this.flatMap { it.toBlocks() }
}


sealed class BlockGroup {
    data class Empty(val size: Int) : BlockGroup() {
        override fun toString(): String {
            return ".".repeat(this.size)
        }

        override fun toBlocks(): List<Block> {
            return List(size) { null }
        }
    }

    data class File(val size: Int, val index: Int) : BlockGroup() {
        override fun toString(): String {
            return index.toString().repeat(this.size)
        }

        override fun toBlocks(): List<Block> {
            return List(size) { index }
        }
    }

    abstract fun toBlocks(): List<Block>
}