package day02

import Day
import DayTest
import org.junit.Test

object DaySolved : Day {

    private val inventrory = BallSet(red = 12, green = 13, blue = 14)

    override fun part1(input: List<String>) = input
            .sumOf { line ->
                val (game, sets) = line.split(":")
                val gameId = game.removePrefix("Game ").toInt()
                val ballSets = sets.split(";").map(BallSet::fromString)

                val isValid = ballSets.all { inventrory.contains(it) }

                if (isValid) gameId else 0
            }

    override fun part2(input: List<String>) = input
            .sumOf { line ->
                val (_, sets) = line.split(":")
                val ballSets = sets.split(";").map(BallSet::fromString)

                ballSets.createMinimalSet().product
            }
}

private fun List<BallSet>.createMinimalSet(): BallSet {
    return BallSet(
            red = maxOf { it.red },
            green = maxOf { it.green },
            blue = maxOf { it.blue },
    )
}

data class BallSet(
        val red: Int,
        val green: Int,
        val blue: Int,
) {

    val product
        get() = red * green * blue

    fun contains(other: BallSet): Boolean {
        return red >= other.red && green >= other.green && blue >= other.blue
    }

    companion object {
        fun fromString(input: String): BallSet {
            val set = input
                    .trim()
                    .split(",")
                    .associate { ball -> ball.trim().split(" ").let { it[1] to it[0].toInt() } }

            return BallSet(
                    red = set.getOrDefault("red", 0),
                    green = set.getOrDefault("green", 0),
                    blue = set.getOrDefault("blue", 0),
            )
        }
    }
}

class DaySolvedTest : DayTest(DaySolved) {

    @Test
    fun testPart1() = testPart1(8)

    @Test
    fun realPart1() = realPart1(2176)

    @Test
    fun testPart2() = testPart2(2286)

    @Test
    fun realPart2() = realPart2(63700)
}