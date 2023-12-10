package day10

import Day
import DayTest
import org.junit.Test
import util.Direction.*
import util.Matrix2D
import util.Point2D

object DaySolved : Day {

    override fun part1(input: List<String>) = input
        .let { Maze(it).solveP1() }

    override fun part2(input: List<String>) = input
        .let { Maze(it).solveP2() }

    class Maze(input: List<String>) {
        private val matrix: Matrix2D
        private val startPoint: Point2D

        init {
            matrix = Matrix2D.create(input)
            startPoint = matrix.find { it == 'S' }
        }

        fun solveP1() = findPath().size / 2
        fun solveP2(): Any {
            val path = findPath()
            matrix.traverse { x, y, _ ->
                if (!path.contains(Point2D(x, y))) {
                    matrix.array[y][x] = '.'
                }
            }

            return matrix.array.mapIndexed { y, row ->
                row.mapIndexed { x, char ->
                    if (!char.isPartOfPath()) {
                        if (checkIfIsIn(x, y)) 1 else 0
                    } else 0
                }
                    .sum()
            }
                .sum()
        }

        private fun checkIfIsIn(x: Int, y: Int): Boolean {
            val count = (0 until x)
                .count { matrix.get(it, y).isCrossingPartOfPath() }
            return (count % 2) == 1
        }

        private fun findPath(): List<Point2D> {
            var prevPoint = startPoint
            var currentPoint = pickInitialMove(startPoint)
            val path = mutableListOf(prevPoint, currentPoint)

            do {
                val nextPoint = findNextPoint(prevPoint, currentPoint)

                path.add(nextPoint)
                prevPoint = currentPoint
                currentPoint = nextPoint
            } while (currentPoint != startPoint)
            return path
        }

        private fun pickInitialMove(start: Point2D): Point2D {
            return start.move(EAST)
        }

        private fun findNextPoint(previous: Point2D, current: Point2D): Point2D {
            return when (matrix.get(current)) {
                '-' -> if (current.x > previous.x) current.move(EAST) else current.move(WEST)

                '|' -> if (current.y > previous.y) current.move(SOUTH) else current.move(NORTH)

                'F' -> if (current.y < previous.y) current.move(EAST) else current.move(SOUTH)

                '7' -> if (current.y < previous.y) current.move(WEST) else current.move(SOUTH)

                'L' -> if (current.y > previous.y) current.move(EAST) else current.move(NORTH)

                'J' -> if (current.y > previous.y) current.move(WEST) else current.move(NORTH)

                else -> throw IllegalStateException("Unknown char $previous $current}")
            }
        }

        private val pathParts = listOf('S', '-', '|', 'F', '7', 'L', 'J')
        private val crossingPathParts = listOf('|', 'F', '7', 'S')
        private fun Char.isPartOfPath(): Boolean {
            return this in pathParts
        }

        private fun Char.isCrossingPartOfPath(): Boolean {
            return this in crossingPathParts
        }
    }
}

class DaySolvedTest : DayTest(DaySolved) {

    @Test
    fun testPart1() = testPart1(4)

    @Test
    fun realPart1() = realPart1(6838)

    @Test
    fun testPart2() = testPart2(4)

    @Test
    fun realPart2() = realPart2(451)
}