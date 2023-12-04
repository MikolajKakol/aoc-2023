package day04

import Day
import DayTest
import org.junit.Test
import util.retrieveNumbers
import kotlin.math.pow

object DaySolved : Day {

    override fun part1(input: List<String>) = input
        .mapNotNull { line ->
            val (_, winning, myNumbers) = line.split(":", "|")
            winning.retrieveNumbers().intersect(myNumbers.retrieveNumbers()).size.takeIf { it > 0 }
        }
        .sumOf { 2f.pow(it - 1).toInt() }

    override fun part2(input: List<String>): Any {
        val cardPiles = input.mapIndexed { index, s -> index to mutableListOf(s) }.toMap()
        cardPiles.keys.sorted().forEach { index ->
            val cardsOfType = cardPiles[index]!!
            val firstCard = cardsOfType[0]

            val (_, winning, myNumbers) = firstCard.split(":", "|")
            val winRate = winning.retrieveNumbers().intersect(myNumbers.retrieveNumbers()).size
            repeat(cardsOfType.size) {
                repeat(winRate) {
                    val list = cardPiles[index + it + 1]!!
                    list.add(list[0])
                }
            }
        }
        return cardPiles.values.map { it.size }.sum()
    }

}

class DaySolvedTest : DayTest(DaySolved) {

    @Test
    fun testPart1() = testPart1(13)

    @Test
    fun realPart1() = realPart1(24706)

    @Test
    fun testPart2() = testPart2(30)

    @Test
    fun realPart2() = realPart2(13114317)
}
