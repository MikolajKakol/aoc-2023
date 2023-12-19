package day18

import Day
import DayTest
import org.junit.Test
import util.*

object DaySolved : Day {

    override suspend fun part1(input: List<String>) = input
        .fold(mutableListOf(Point2D(0, 0))) { acc, s ->
            val (dirStr, lenStr, _) = s.split(" ")
            val direction = dirStr.toDirection()

            val start = acc.last()
            val element = start.move(direction, lenStr.toInt())
            acc.add(element)
            acc
        }
        .let { points -> countPoints(points) }

    @OptIn(ExperimentalStdlibApi::class)
    override suspend fun part2(input: List<String>) = input
        .fold(mutableListOf(Point2D(0, 0))) { acc, s ->
            val color = s.split(" ").last().drop(2).dropLast(1)
            val dirStr = color.last()
            val lenStr = color.dropLast(1)
            val direction = dirStr.toDirection()

            val start = acc.last()
            val distance = lenStr.hexToInt()
            val element = start.move(direction, distance)
            acc.add(element)
            acc
        }
        .let { points -> countPoints(points) }

    private fun countPoints(lines: List<Point2D>): Long {
        val area = lines
            .windowed(2, 1)
            .sumOf { (a, b) -> a.x.toLong() * b.y.toLong() - b.x.toLong() * a.y.toLong() } / 2

        val border = lines
            .windowed(2, 1)
            .sumOf { (a, b) -> a.manhattanDistance(b) }

        val interior = area - border / 2 + 1
        return border + interior
    }

    private fun String.toDirection(): Direction {
        return when (this) {
            "R" -> Direction.EAST
            "L" -> Direction.WEST
            "U" -> Direction.NORTH
            "D" -> Direction.SOUTH
            else -> throw IllegalArgumentException("Unknown direction $this")
        }
    }

    private fun Char.toDirection(): Direction {
        return when (this) {
            '0' -> Direction.EAST
            '2' -> Direction.WEST
            '3' -> Direction.NORTH
            '1' -> Direction.SOUTH
            else -> throw IllegalArgumentException("Unknown direction $this")
        }
    }

}

class DaySolvedTest : DayTest(DaySolved, true) {

    @Test
    fun testPart1() = testPart1(62L)

    @Test
    fun realPart1() = realPart1(58550L)

    @Test
    fun testPart2() = testPart2(952408144115L)

    @Test
    fun realPart2() = realPart2(47452118468566L)
}