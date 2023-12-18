package day16

import Day
import DayTest
import org.junit.Test
import parallelMap
import util.*
import kotlin.test.fail

object DaySolved : Day {

    override suspend fun part1(input: List<String>) = Maze(input, DirectedPoint2D(Point2D(0, 0), Direction.EAST)).solve()

    override suspend fun part2(input: List<String>) : Int = input
        .let {
            val width = input[0].length
            listOf(
                input.indices.map { y ->
                    Point2D(0, y) with Direction.EAST
                },
                input.indices.map { y ->
                    Point2D(width - 1, y) with Direction.WEST
                },
                (0 until width).map { x ->
                    Point2D(x, 0) with Direction.SOUTH
                },
                (0 until width).map { x ->
                    Point2D(x, input.size - 1) with Direction.NORTH
                }
            )
                .flatten()
                .parallelMap {  Maze(input, it).solve() }
                .max()
        }

    class Maze(input: List<String>, private val startPosition: DirectedPoint2D) {
        private val matrix = Matrix2D.create(input)
        private val energyPoints = Matrix2D.create(input)
        private val deque = ArrayDeque<DirectedPoint2D>()
        private val history = mutableSetOf<DirectedPoint2D>()

        private val maxBounces = 40000

        fun solve(): Int {

            deque.apply { add(startPosition) }
            var bounceCount = 0

            while (deque.isNotEmpty()) {
                if (bounceCount++ > maxBounces) fail("Too many bounces $deque")
                bounceCount++

                val pointDirected = deque.removeFirst()
                val (point, direction) = pointDirected
                val value = matrix.getOrNull(point) ?: continue
                energyPoints.set(point, '#')

                when {
                    value == '.'
                            || (value == '-' && direction.isHorizontal)
                            || (value == '|' && direction.isVertical) ->
                        add(pointDirected, direction)

                    value == '/' ->
                        when (direction) {
                            Direction.NORTH -> Direction.EAST
                            Direction.SOUTH -> Direction.WEST
                            Direction.EAST -> Direction.NORTH
                            Direction.WEST -> Direction.SOUTH
                        }
                            .let { deque.add(pointDirected.move(it)) }

                    value == '\\' ->
                        when (direction) {
                            Direction.NORTH -> Direction.WEST
                            Direction.SOUTH -> Direction.EAST
                            Direction.EAST -> Direction.SOUTH
                            Direction.WEST -> Direction.NORTH
                        }
                            .let { deque.add(pointDirected.move(it)) }

                    value == '-' -> {
                        add(pointDirected, Direction.EAST)
                        add(pointDirected, Direction.WEST)
                    }

                    value == '|' -> {
                        add(pointDirected, Direction.NORTH)
                        add(pointDirected, Direction.SOUTH)
                    }
                }
            }
            return energyPoints.findAll { it == '#' }.size
        }

        private fun add(pointDirected: DirectedPoint2D, direction: Direction) {
            val element = pointDirected.move(direction)
            if (!history.contains(element)) {
                history.add(element)
                deque.add(element)
            }
        }
    }

}

class DaySolvedTest : DayTest(DaySolved, true) {

    @Test
    fun testPart1() = testPart1(46)

    @Test
    fun realPart1() = realPart1(7498)

    @Test
    fun testPart2() = testPart2(51)

    @Test
    fun realPart2() = realPart2(7846)
}