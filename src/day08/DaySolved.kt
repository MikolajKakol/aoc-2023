package day08

import Day
import DayTest
import lcm
import org.junit.Test

object DaySolved : Day {

    override suspend fun part1(input: List<String>): Any {
        lateinit var instructions: String
        val nodes: MutableMap<String, Pair<String, String>> = mutableMapOf()
        return input
            .forEach {
                when {
                    it.isEmpty() -> Unit
                    it.length == 16 -> {
                        val key = it.substring(0..2)
                        val left = it.substring(7..9)
                        val right = it.substring(12..14)
                        nodes[key] = left to right
                    }

                    else -> instructions = it
                }
            }
            .let { startNavigation(instructions, nodes, "AAA") }
    }

    private fun startNavigation(
        instructions: String,
        nodes: Map<String, Pair<String, String>>,
        startPoint: String,
        end: (String) -> Boolean = { it == "ZZZ" }
    ): Int {
        var stepCount = 0
        var found = false
        var currentNode = nodes[startPoint]!!
        while (!found) {
            val instruction = instructions[stepCount % instructions.length]
            val next = if (instruction == 'L') currentNode.first else currentNode.second

            if (end(next)) {
                found = true
            }
            currentNode = nodes[next]!!
            stepCount++
        }

        return stepCount
    }

    override suspend fun part2(input: List<String>): Any {
        lateinit var instructions: String
        val nodes: MutableMap<String, Pair<String, String>> = mutableMapOf()
        return input
            .forEach {
                when {
                    it.isEmpty() -> Unit
                    it.length == 16 -> {
                        val key = it.substring(0..2)
                        val left = it.substring(7..9)
                        val right = it.substring(12..14)
                        nodes[key] = left to right
                    }

                    else -> instructions = it
                }
            }
            .let {
                nodes.keys.filter { it[2] == 'A' }
                    .map { startNavigation(instructions, nodes, it) { it[2] == 'Z' } }
                    .map { it.toLong() }
                    .toTypedArray()
                    .toLongArray()
                    .let { lcm(it) }
            }
    }
}

class DaySolvedTest : DayTest(DaySolved, true) {

    @Test
    fun testPart1() = testPart1(2)

    @Test
    fun realPart1() = realPart1(21251)

    @Test
    fun testPart2() = solvePart2(6L, "test2")

    @Test
    fun realPart2() = realPart2(11_678_319_315_857L)
}