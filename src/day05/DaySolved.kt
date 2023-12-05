package day05

import Day
import DayTest
import org.junit.Test
import util.retrieveLongNumbers

object DaySolved : Day {

    override fun part1(input: List<String>) = input
        .joinToString("\n")
        .split("\n\n")
        .let { chunks ->
            val seeds = chunks[0].retrieveLongNumbers()
            val mappings = chunks
                .drop(1)
                .map { it.createMapping() }

            seeds.map {
                mappings.fold(it) { current, mapping ->
                    mapping.convert(current)
                }
            }

        }
        .min()

    override fun part2(input: List<String>) = input
        .joinToString("\n")
        .split("\n\n")
        .let { chunks ->
            val seeds = chunks[0].retrieveLongNumbers().windowed(2, 2)
            val mappings = chunks
                .drop(1)
                .map { it.createMapping() }

            sequence {
                seeds.forEach { seedBags ->
                    val (seedStart, count) = seedBags
                    val times = count.toInt()

                    repeat(times) {
                        yield(seedStart + it)
                    }
                }
            }
                .map { mappings.fold(it) { current, mapping -> mapping.convert(current) } }
                .min()
        }

    private fun String.createMapping(): Mapping {
        return Mapping(
            this
                .split("\n")
                .drop(1)
                .map { Rule(it.retrieveLongNumbers()) }
        )
    }

    data class Mapping(val rules: List<Rule>) {

        fun convert(number: Long): Long {
            return rules.firstOrNull { it.matches(number) }?.convert(number) ?: number
        }
    }

    class Rule(numbers: List<Long>) {
        val destination = numbers[1]
        val source = numbers[0]
        val range = numbers[2]

        val diff = destination - source

        fun matches(number: Long): Boolean {
            return destination + range > number && number >= destination
        }

        fun convert(number: Long): Long {
            return number - diff
        }

        override fun toString(): String {
            return "Rule: dest: $destination, src: $source, rng: $range"
        }
    }
}

class DaySolvedTest : DayTest(DaySolved) {

    @Test
    fun testPart1() = testPart1(35L)

    @Test
    fun realPart1() = realPart1(322500873L)

    @Test
    fun testPart2() = testPart2(46L)

    @Test
    fun realPart2() = realPart2(108956227L)
}