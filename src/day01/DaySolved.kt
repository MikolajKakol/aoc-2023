package day01

import Day
import DayTest
import org.junit.Test

object DaySolved : Day {

    override suspend fun part1(input: List<String>): Int = input
            .map { text ->
                val first = text.first { it.isDigit() }.toString()
                val second = text.last { it.isDigit() }.toString()
                first + second
            }
            .sumOf { it.toInt() }

    override suspend fun part2(input: List<String>): Int = input
            .map { text ->
                var first = ""
                var second = ""
                var t = text
                do {
                    val found = digitsString.find { t.startsWith(it) }
                    if (found != null) {
                        first = found.stringToIntString()
                    } else {
                        t = t.drop(1)
                    }

                } while (first.isEmpty())

                t = text
                do {
                    val found = digitsString.find { t.endsWith(it) }
                    if (found != null) {
                        second = found.stringToIntString()
                    } else {
                        t = t.dropLast(1)
                    }

                } while (second.isEmpty())

                first + second
            }
            .let { part1(it) }

    private fun String.stringToIntString(): String {
        val indexOf = digitsAsString.indexOf(this)
        val first = if (indexOf >= 0) {
            (indexOf + 1).toString()
        } else {
            this
        }
        return first
    }

    private val digitsAsString = listOf(
            "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
    )
    private val digitsString = (0..9).map { it.toString() } + digitsAsString
}


class DaySolvedTest : DayTest(DaySolved) {

    @Test
    fun testPart1() = testPart1(142)

    @Test
    fun realPart1() = realPart1(54605)

    @Test
    fun testPart2() = testPart2(281)

    @Test
    fun realPart2() = realPart2(55429)
}