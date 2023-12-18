package day07

import Day
import DayTest
import day07.HandType.Companion.handType
import org.junit.Test
import util.println

object DaySolved : Day {

    override suspend fun part1(input: List<String>) = input
        .map {
            val (hand, bid) = it.split(" ")
            Deal(Hand(hand.map(::Card)), bid.toInt())
        }
        .sortedWith(compareBy(
            { it.hand.strength },
            { it.hand.cards.joinToString { it.strengthHex } }
        ))
        .mapIndexed { index, deal -> (index + 1).toLong() * deal.bid.toLong() }
        .sum()

    override suspend fun part2(input: List<String>) = input
        .map {
            val (hand, bid) = it.split(" ")
            Deal(Hand(hand.map(::Card)), bid.toInt())
        }
        .sortedWith(compareBy(
            { it.hand.strengthJoker },
            { it.hand.cards.joinToString { it.strengthJokerHex } }
        ))
        .mapIndexed { index, deal -> (index + 1).toLong() * deal.bid.toLong() }
        .sum()

}

data class Deal(val hand: Hand, val bid: Int)

class Hand(val cards: List<Card>) {
    private val cardsString = cards.joinToString(separator = "") { it.char.toString() }

    val strength: HandType = run {
        val grouping = cards.groupBy { it.char }
        handType(grouping.values.sortedByDescending { it.size })
    }

    val strengthJoker = run {
        val grouping = cards.groupBy { it.char }
        val jokers = grouping['J'] ?: emptyList()
        val values = grouping.values.sortedByDescending { it.size }
            .filter { it.isNotEmpty() && it[0].char != 'J' } + listOf(jokers)
        val handType = handType(values)
        handType.upgradeWithJokers(jokers.size)
            .also {
                if (jokers.size > 0 && handType == HandType.HIGH_CARD && jokers.size ==3)
                    "$cardsString upgraded $handType to $it with ${jokers.size} jokers".println()
            }
    }

    override fun toString(): String {
        return "Hand=$cardsString, str=$strength"
    }
}

enum class HandType {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_OF_KIND,
    FULL_HOUSE,
    FOUR_OF_KIND,
    FIVE_OF_KIND,
    ;

    fun upgradeWithJokers(count: Int): HandType {
        return when (this) {
            HIGH_CARD -> when (count) {
                4 -> FIVE_OF_KIND
                3 -> FOUR_OF_KIND
                2 -> THREE_OF_KIND
                1 -> ONE_PAIR
                else -> HIGH_CARD
            }

            ONE_PAIR -> when (count) {
                3 -> FIVE_OF_KIND
                2 -> FOUR_OF_KIND
                1 -> THREE_OF_KIND
                else -> ONE_PAIR
            }

            TWO_PAIR -> when (count) {
                1 -> FULL_HOUSE
                else -> TWO_PAIR
            }

            THREE_OF_KIND -> when (count) {
                2 -> FIVE_OF_KIND
                1 -> FOUR_OF_KIND
                else -> THREE_OF_KIND
            }

            FULL_HOUSE -> when (count) {
                2 -> FIVE_OF_KIND
                1 -> FOUR_OF_KIND
                else -> FULL_HOUSE
            }

            FOUR_OF_KIND -> when (count) {
                1 -> FIVE_OF_KIND
                else -> FOUR_OF_KIND
            }

            FIVE_OF_KIND -> FIVE_OF_KIND
        }
    }

    companion object {

        fun handType(values: List<List<Card>>) = when {
            values[0].size == 5 -> FIVE_OF_KIND
            values[0].size == 4 -> FOUR_OF_KIND
            values[0].size == 3 && values[1].size == 2 -> FULL_HOUSE
            values[0].size == 3 && values[1].size == 1 -> THREE_OF_KIND
            values[0].size == 2 && values[1].size == 2 -> TWO_PAIR
            values[0].size == 2 -> ONE_PAIR
            else -> HIGH_CARD
        }
    }

}

class Card(val char: Char) {

    private val strength: Int
        get() {
            return when {
                char.isDigit() -> char.digitToInt()
                char == 'T' -> 10
                char == 'J' -> 11
                char == 'Q' -> 12
                char == 'K' -> 13
                char == 'A' -> 14
                else -> 0
            }
        }
    private val strengthJoker: Int
        get() {
            return when {
                char.isDigit() -> char.digitToInt()
                char == 'T' -> 10
                char == 'J' -> 1
                char == 'Q' -> 12
                char == 'K' -> 13
                char == 'A' -> 14
                else -> 0
            }
        }

    @OptIn(ExperimentalStdlibApi::class)
    val strengthHex = strength.toHexString()

    @OptIn(ExperimentalStdlibApi::class)
    val strengthJokerHex = strengthJoker.toHexString()

    override fun toString(): String {
        return char.toString()
    }
}

class DaySolvedTest : DayTest(DaySolved, true) {

    @Test
    fun testPart1() = testPart1(6440L)

    @Test
    fun realPart1() = realPart1(250957639L)

    @Test
    fun testPart2() = testPart2(5905L)

    @Test
    fun realPart2() = realPart2(251515496L)
}