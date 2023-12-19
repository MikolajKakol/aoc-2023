package day19

import Day
import DayTest
import org.junit.Test

object DaySolved : Day {

    override suspend fun part1(input: List<String>) = input
        .joinToString("\n")
        .split("\n\n")
        .let { chunks ->
            val rules = chunks[0]
            val parts = chunks[1]

            val workflow = rules.split("\n")
                .map { line ->
                    val (name, ruleSetStr) = line.split("{")
                    RuleSet(name, createRules(ruleSetStr))
                }
                .let { Workflow(it) }

            val partList = parts.split("\n")
                .map {
                    val properties = it
                        .drop(1)
                        .dropLast(1)
                        .split(",")
                        .associate { propertyStr ->
                            val (name, value) = propertyStr.split("=")
                            Property(name) to Value(value.toInt())
                        }
                    Part(properties)
                }

            partList
                .filter { workflow.checkValid(it) }
                .sumOf { it.value }

        }

    private fun createRules(ruleSetStr: String): List<Rule> = ruleSetStr
        .dropLast(1)
        .split(",")
        .map {
            val ruleStr = it.split(":")
            if (ruleStr.size == 1) {
                Rule(
                    matcher = { ruleStr[0].toResult() },
                )
            } else {
                val (propertyStr, valueStr) = ruleStr[0].split(">", "<")
                val isGraterThan = ruleStr[0].contains(">")
                Rule(
                    matcher = { part ->
                        val property = Property(propertyStr)
                        val value = Value(valueStr.toInt())
                        part.properties[property]
                            ?.let { partValue ->
                                if (isGraterThan) {
                                    if (partValue.magnitude > value.magnitude) {
                                        ruleStr[1].toResult()
                                    } else {
                                        null
                                    }
                                } else {
                                    if (partValue.magnitude < value.magnitude) {
                                        ruleStr[1].toResult()
                                    } else {
                                        null
                                    }
                                }
                            }
                    },
                )
            }
        }

    override suspend fun part2(input: List<String>) = input


    @JvmInline
    value class Property(val name: String)

    @JvmInline
    value class Value(val magnitude: Int)

    @JvmInline
    value class Part(val properties: Map<Property, Value>) {
        val value: Int
            get() = properties.values.sumOf { it.magnitude }
    }

    class Rule(
        val matcher: (Part) -> RuleResult?,
    )

    sealed interface RuleResult {
        data object Accepted : RuleResult
        data object Rejected : RuleResult
        class Chain(val ruleName: String) : RuleResult
    }

    class RuleSet(val name: String, val rules: List<Rule>) {
        fun apply(part: Part): RuleResult {
            return rules
                .firstNotNullOf { rule -> rule.matcher(part) }
        }
    }

    class Workflow(val ruleSets: List<RuleSet>) {
        fun checkValid(part: Part): Boolean {
            val ruleSet = ruleSets.first { it.name == "in" }
            return checkValid(part, ruleSet) == RuleResult.Accepted
        }

        private fun checkValid(part: Part, ruleSet: RuleSet): RuleResult {
            return when (val r = ruleSet.apply(part)) {
                RuleResult.Accepted -> RuleResult.Accepted
                RuleResult.Rejected -> RuleResult.Rejected
                is RuleResult.Chain -> {
                    val nextRuleSet = ruleSets.first { r.ruleName == it.name }
                    checkValid(part, nextRuleSet)
                }
            }
        }
    }

    private fun String.toResult(): RuleResult {
        return when (this) {
            "A" -> RuleResult.Accepted
            "R" -> RuleResult.Rejected
            else -> RuleResult.Chain(this)
        }
    }
}

class DaySolvedTest : DayTest(DaySolved) {

    @Test
    fun testPart1() = testPart1(19114)

    @Test
    fun realPart1() = realPart1("SOLVE ME")

    @Test
    fun testPart2() = testPart2("SOLVE ME")

    @Test
    fun realPart2() = realPart2("SOLVE ME")
}