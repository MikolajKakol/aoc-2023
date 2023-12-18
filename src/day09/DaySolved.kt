package day09

import Day
import DayTest
import org.junit.Test
import util.retrieveNumbers

object DaySolved : Day {

    override suspend fun part1(input: List<String>) = input
        .sumOf { it.retrieveNumbers().predictNext() }

    override suspend fun part2(input: List<String>) = input
        .sumOf {
            it.retrieveNumbers()
                .predictNext(
                    operation = { a, b -> a - b },
                    select = { first() },
                )
        }

    private fun Iterable<Int>.predictNext(
        operation: (Int, Int) -> Int = { a, b -> a + b },
        select: Iterable<Int>.() -> Int = { last() },
    ): Int {
        val newEval = windowed(2, 1)
            .map { (a, b) -> b - a }

        return if (newEval.all { it == 0 }) {
            operation(select(this), select(newEval))
        } else {
            operation(select(this), newEval.predictNext(operation, select))
        }
    }
}

class DaySolvedTest : DayTest(DaySolved, true) {

    @Test
    fun testPart1() = testPart1(114)

    @Test
    fun realPart1() = realPart1(1930746032)

    @Test
    fun testPart2() = testPart2(2)

    @Test
    fun realPart2() = realPart2(1154)
}