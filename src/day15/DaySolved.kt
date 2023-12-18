package day15

import Day
import DayTest
import org.junit.Test

object DaySolved : Day {

    override suspend fun part1(input: List<String>) = input
        .joinToString()
        .split(",")
        .sumOf { it.hash() }

    private fun String.hash(): Int = fold(0) { acc, c ->
        ((c.code + acc) * 17) % 256
    }

    override suspend fun part2(input: List<String>) = input
        .joinToString()
        .split(",")
        .groupBy {
            val (label) = it.split("-", "=")
            label.hash()
        }
        .values
        .map { amounts ->
            val ar = mutableListOf<LensInfo>()
            amounts
                .map(LensInfo.Companion::fromString)
                .forEach { lens ->
                    if (lens.isErase) {
                        ar.removeAll { it.label == lens.label }
                    } else {
                        val index = ar.indexOfFirst { it.label == lens.label }
                        if (index >= 0) ar[index] = lens
                        else ar.add(lens)
                    }
                }
            ar
        }
        .sumOf { lenses ->
            lenses
                .mapIndexed { slot, lens -> lens.boxNo * (lens.amount ?: 0) * (slot + 1) }
                .sum()
        }

    data class LensInfo(val label: String, val amount: Int?, val isErase: Boolean) {

        val boxNo = label.hash() + 1

        override fun toString(): String {
            return "$label=${amount ?: ""}"
        }

        companion object {
            fun fromString(str: String): LensInfo {
                val isDash = str.contains("-")
                val (label, amount) = str.split("-", "=")
                return LensInfo(label, amount.toIntOrNull(), isDash)
            }
        }
    }
}

class DaySolvedTest : DayTest(DaySolved, true) {

    @Test
    fun testPart1() = testPart1(1320)

    @Test
    fun realPart1() = realPart1(512283)

    @Test
    fun testPart2() = testPart2(145)

    @Test
    fun realPart2() = realPart2(215827)
}