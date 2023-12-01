package day02

import Day
import DayTest
import org.junit.Test

object DaySolved : Day {

    override fun part1(input: List<String>) = input

    override fun part2(input: List<String>) = input

}

class DaySolvedTest : DayTest(DaySolved) {

    @Test
    fun testPart1() = testPart1("SOLVE ME")

    @Test
    fun realPart1() = realPart1("SOLVE ME")

    @Test
    fun testPart2() = testPart2("SOLVE ME")

    @Test
    fun realPart2() = realPart2("SOLVE ME")
}