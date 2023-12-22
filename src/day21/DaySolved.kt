package day21

import Day
import DayTest
import kotlinx.coroutines.isActive
import org.junit.Before
import org.junit.Test
import util.*
import java.util.ArrayDeque
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration.Companion.seconds

class DaySolved(val stepCount: Int) : Day {

    override suspend fun part1(input: List<String>): Int = input
        .let {
            val maze = Matrix2D.create(it)
            val start = maze.find('S')
            maze.set(start, '.')
            arrayDeque.add(stepCount to start)
            while (arrayDeque.isNotEmpty() && coroutineContext.isActive) {
                val (steps, pos) = arrayDeque.removeFirst()
                maze.countPositions(steps, pos)
            }
            visited.size
        }

    override suspend fun part2(input: List<String>) = part1(input)


    val memo = mutableMapOf<String, Boolean>()
    val visited = mutableSetOf<String>()
    val arrayDeque = ArrayDeque<Pair<Int, Point2D>>()

    private fun Matrix2D.countPositions(steps: Int, position: Point2D) {
        val key = "$steps:$position"
        memo[key]?.let { return }
        when (steps) {
            0 -> Unit
            1 -> countPaths(position)
            else -> Direction.entries
                .forEach { direction ->
                    val next = position.move(direction)
                    if (getInfinite(next) == '.') {
                        arrayDeque.add(steps - 1 to next)
                    }
                }
        }
            .also {
//                "caching $key".println()
                memo[key] = true
            }
    }

    private fun Matrix2D.countPaths(position: Point2D) {
        Direction.entries
            .mapNotNull {
                val next = position.move(it)
                if (getInfinite(next) == '.') {
                    visited.add(next.toString())
                    next
                } else {
                    null
                }
            }
    }
}

class DaySolvedTest : DayTest(DaySolved(6), true) {

    @Before
    fun before() {
        maxTestTime = 5.seconds
    }

    @Test
    fun testPart1() = testPart1(16)

    @Test
    fun realPart1() {
        day = DaySolved(64)
        realPart1(3578)
    }

    @Test
    fun testPart2_500() {
        val daySolved = DaySolved(500)
        day = daySolved
        testPart2(167004)
    }

    @Test
    fun testPart2_10() {
        val daySolved = DaySolved(10)
        day = daySolved
        testPart2(50)
    }

    @Test
    fun testPart2_50() {
        val daySolved = DaySolved(50)
        day = daySolved
        testPart2(1594)
    }

    @Test
    fun testPart2_100() {
        val daySolved = DaySolved(100)
        day = daySolved
        testPart2(6536)
    }

    @Test
    fun testPart2_1000() {
        val daySolved = DaySolved(1000)
        day = daySolved
        testPart2(668697)
    }

    @Test
    fun realPart2() {
        day = DaySolved(26501365)
//        realPart2("SOLVE ME")
    }
}