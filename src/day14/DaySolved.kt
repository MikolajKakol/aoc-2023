package day14

import Day
import DayTest
import org.junit.Test
import util.Matrix2D


object DaySolved : Day {

    override fun part1(input: List<String>) = input
        .let { RockDish(it) }
        .solve()

    override fun part2(input: List<String>) = input

    class RockDish(input: List<String>) {
        private val matrix = Matrix2D.create(input)

        fun solve(): Any {
            matrix.rows.forEachIndexed { y, chars ->
                chars.forEachIndexed { x, char ->
                    if (char.isRounded) {
                        moveUp(x, y)
                    }
                }
            }
            return matrix.rows.reversed()
                .mapIndexed { index, chars ->
                    chars.count { it.isRounded } * (index + 1)
                }
                .sum()
        }

        private tailrec fun moveUp(x: Int, y: Int) {
            val itemAbove = matrix.getOrNull(x, y - 1)
            if (itemAbove.hasSpace) {
                matrix.set(x, y - 1, 'O')
                matrix.set(x, y, '.')
                moveUp(x, y - 1)
            }
        }

        private val Char.isRounded: Boolean
            get() = this == 'O'

        private val Char?.hasSpace: Boolean
            get() = this == '.'
    }
}

class DaySolvedTest : DayTest(DaySolved) {

    @Test
    fun testPart1() = testPart1(136)

    @Test
    fun realPart1() = realPart1(109385)

    @Test
    fun testPart2() = testPart2("SOLVE ME")

    @Test
    fun realPart2() = realPart2("SOLVE ME")
}