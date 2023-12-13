package day13

import Day
import DayTest
import org.junit.Test
import util.Matrix2D

object DaySolved : Day {

    override fun part1(input: List<String>) = input
        .joinToString("\n")
        .split("\n\n")
        .map { MirrorScheme(it) }
        .sumOf { it.solve() }

    override fun part2(input: List<String>) = input
        .joinToString("\n")
        .split("\n\n")
        .map { MirrorScheme(it, 1) }
        .sumOf { it.solve() }

    class MirrorScheme(input: String, private val count: Int = 0) {
        private val matrix = Matrix2D.create(input.split("\n"))

        fun solve(): Int {
            val row = find(matrix.rows)
            val column = find(matrix.columns)
            return column ?: (row!! * 100)
        }

        private fun find(list: List<CharArray>): Int? {
            val items = list.map { String(it) }
            (0 until items.size - 1).forEach { index ->
                var a = items.slice(0..index).reversed().joinToString("")
                var b = items.slice(index + 1 until items.size).joinToString("")
                if (a.length > b.length) {
                    a = a.slice(b.indices)
                } else {
                    b = b.slice(a.indices)
                }

                if (a.indices.count { a[it] != b[it] } == this.count) return index + 1
            }
            return null
        }

    }
}

class DaySolvedTest : DayTest(DaySolved, true) {

    @Test
    fun testPart1() = testPart1(405)

    @Test
    fun realPart1() = realPart1(28651)

    @Test
    fun testPart2() = testPart2(400)

    @Test
    fun realPart2() = realPart2(25450)
}