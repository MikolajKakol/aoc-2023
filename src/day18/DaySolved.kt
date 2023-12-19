package day18

import Day
import DayTest
import org.junit.Test
import util.*
import kotlin.math.absoluteValue

object DaySolved : Day {

    override suspend fun part1(input: List<String>) = input
        .fold(mutableListOf(Line.zero)) { acc, s ->
            val (dirStr, lenStr, color) = s.split(" ")
            val direction = dirStr.toDirection()

            val start = acc.last().end
            val end = start.move(direction, lenStr.toInt())
            acc.add(Line(start, end, direction, color))
            acc
        }
        .drop(1)
        .let { lines ->
            val width = lines.maxOf { line -> line.end.x } + 1
            val widthNeg = lines.minOf { line -> line.end.x } - 1
            val height = lines.maxOf { line -> line.end.y } + 1
            val heightNeg = lines.minOf { line -> line.end.y } - 1

            val verticalLines = lines.filter { line -> line.direction.isVertical }

            val insidePoints = sequence {
                (heightNeg until height).forEach { y ->
                    (widthNeg until width).forEach { x ->
                        yield(Point2D(x, y))
                    }
                }
            }
                .count { point ->
                    val count = verticalLines.sumOf { line ->
                        // TODO report bug to Kotlin
                        if (line.start.x >= point.x) {
                            if (line.hasY(point.y) && lines.none { it.has(point) }) {
                                if (line.direction == Direction.NORTH) {
                                    1
                                } else {
                                    -1
                                }
                            } else {
                                0
                            }

                        } else {
                            0
                        } as Int
                    }.absoluteValue

                    count % 2 == 1
                }
            insidePoints + lines.sumOf { it.length }
        }

    override suspend fun part2(input: List<String>) = input

    data class Line(val start: Point2D, val end: Point2D, val direction: Direction, val color: String) {

        companion object {
            val zero = Line(Point2D(0, 0), Point2D(0, 0), Direction.EAST, "white")
        }

        val length = start.manhattanDistance(end)

        fun hasY(y: Int): Boolean {
            return when (direction) {
                Direction.NORTH -> {
                    start.y >= y && y > end.y
                }
                Direction.SOUTH -> {
                    y > start.y && y <= end.y
                }
                else -> {
                    throw IllegalStateException("Unknown direction $direction")

                }
            }
        }

        fun has(point: Point2D): Boolean {
            if (direction.isHorizontal) {
                if (point.y != start.y) return false
                return point.x in start.x..end.x || point.x in end.x..start.x
            } else {
                if (point.x != start.x) return false
                return point.y in start.y..end.y || point.y in end.y..start.y

            }
        }
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
}

class DaySolvedTest : DayTest(DaySolved) {

    @Test
    fun testPart1() = testPart1(62)

    @Test
    fun realPart1() = realPart1(58550)

    @Test
    fun testPart2() = testPart2("SOLVE ME")

    @Test
    fun realPart2() = realPart2("SOLVE ME")
}