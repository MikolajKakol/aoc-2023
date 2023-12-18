package day06

import Day
import DayTest
import org.junit.Test
import util.retrieveLongNumbers
import util.retrieveNumbers

object DaySolved : Day {

    private lateinit var availableTimes: List<Int>
    private lateinit var distanceRecords: List<Long>

    override suspend fun part1(input: List<String>) = input
        .forEachIndexed { index, s ->
            when (index) {
                0 -> availableTimes = s.retrieveNumbers()
                1 -> distanceRecords = s.retrieveLongNumbers()
            }
        }
        .let {
            availableTimes.mapIndexed { index, availableTime ->
                val distanceRecord = distanceRecords[index]
                availableTime.calculateDistances()
                    .filter { it > distanceRecord }
                    .count()
            }.reduce { acc, i -> acc * i }
        }

    override suspend fun part2(input: List<String>) = input
        .forEachIndexed { index, s ->
            when (index) {
                0 -> availableTimes = s.replace(" ", "").retrieveNumbers()
                1 -> distanceRecords = s.replace(" ", "").retrieveLongNumbers()
            }
        }
        .let {
            val time = availableTimes[0]
            val distance = distanceRecords[0]

            time.calculateDistances()
                .filter { it > distance }
                .count()
        }


    private fun Int.calculateDistances(): Sequence<Long> {
        val availableTime = this
        return sequence {
            repeat(availableTime + 1) {
                val velocity = it
                val remainingTime = availableTime - it
                val distance = velocity.toLong() * remainingTime.toLong()
                yield(distance)
            }
        }
    }
}

class DaySolvedTest : DayTest(DaySolved, hasSameInputBetweenDays = true) {

    @Test
    fun testPart1() = testPart1(288)

    @Test
    fun realPart1() = realPart1(2344708)

    @Test
    fun testPart2() = testPart2(71503)

    @Test
    fun realPart2() = realPart2(30125202)
}