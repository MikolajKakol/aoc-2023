package day12

import Day
import DayTest
import org.junit.Test
import util.retrieveNumbers

object DaySolved : Day {

    override suspend fun part1(input: List<String>) = input
        .sumOf { line ->
            val (pattern, pipesInfo) = line.split(" ")
            val pipes = pipesInfo.retrieveNumbers()
            val foundPipes = pattern.count { it == '#' }
            val possiblePipes = pattern.count { it == '?' }
            val missingPipes = pipes.sum() - foundPipes

            val permutations = generatePermutations(possiblePipes, missingPipes)
            permutations
                .map {
                    val array = pattern.toCharArray()
                    it.forEach { c -> array.replaceFirst('?', c) }
                    String(array)
                }
                .count { possiblePattern ->
                    possiblePattern.split(".")
                        .filter { it.isNotEmpty() }
                        .map { it.length } == pipes
                }
        }

    override suspend fun part2(input: List<String>) = input
        .sumOf { line ->
            val (p, i) = line.split(" ")
            val pattern = "$p?$p?$p?$p?$p"
            val pipes = "$i $i $i $i $i".retrieveNumbers()
            val foundPipes = pattern.count { it == '#' }
            val possiblePipes = pattern.count { it == '?' }
            val missingPipes = pipes.sum() - foundPipes

            val permutations = generatePermutations(possiblePipes, missingPipes)
            permutations
                .map {
                    val array = pattern.toCharArray()
                    it.forEach { c -> array.replaceFirst('?', c) }
                    String(array)
                }
                .count { possiblePattern ->
                    possiblePattern.split(".")
                        .filter { it.isNotEmpty() }
                        .map { it.length } == pipes
                }
        }

    private val cachePermutations = mutableMapOf<Pair<Int, Int>, List<String>>()

    private fun generatePermutations(n: Int, count: Int): List<String> {
        cachePermutations[n to count]?.let { return it }

        val values = charArrayOf('#', '.')
        val k = values.size
        val pn = IntArray(n)
        val pc = CharArray(n)
        val permutations = mutableListOf<String>()
        while (true) {
            // generate permutation
            for ((i, x) in pn.withIndex()) pc[i] = values[x]
            // add permutation to list
            if (pc.count { it == '#' } == count) {
                permutations.add(String(pc))
            }
            // increment permutation number
            var i = 0
            while (true) {
                pn[i]++
                if (pn[i] < k) break
                pn[i++] = 0
                if (i == n) return permutations
                    .also { cachePermutations[n to count] = it }
                // all permutations generated
            }
        }

    }
}

private fun CharArray.replaceFirst(old: Char, new: Char): Boolean {
    for (i in this.indices) {
        if (this[i] == old) {
            this[i] = new
            return true
        }
    }
    return false
}

class DaySolvedTest : DayTest(DaySolved, true) {

    @Test
    fun testPart1() = testPart1(21)

    @Test
    fun realPart1() = realPart1(6871)

    @Test
    fun testPart2() = testPart2(525152)

    @Test
    fun realPart2() = realPart2("SOLVE ME")
}