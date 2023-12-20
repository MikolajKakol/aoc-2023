package day20

import Day
import DayTest
import day20.DaySolved.Signal.high
import day20.DaySolved.Signal.low
import lcm
import org.junit.Test
import util.println
import kotlin.test.fail

object DaySolved : Day {

    override suspend fun part1(input: List<String>) = input
        .let { lines -> processor(lines).solve1() }

    override suspend fun part2(input: List<String>) = input
        .let { lines -> processor(lines).solve2() }

    private fun processor(lines: List<String>): Processor {
        val modules = lines.map {
            val module = it.split(" ->")[0]
            when (module.first()) {
                'b' -> BroadcastModule(module)
                '%' -> FlipFlopModule(module.drop(1))
                '&' -> ConjunctionModule(module.drop(1))
                else -> throw IllegalArgumentException("Unknown module type: ${module.first()}")
            }
        }.toMutableList()

        val connections = lines.map { line ->
            var (source, destinations) = line.split(" -> ")
            if (source.first() != 'b') {
                source = source.drop(1)
            }

            val sourceModule = modules.first { it.name == source }
            val destinationModules = destinations.split(", ").map { name ->
                modules.firstOrNull { it.name == name } ?: run {
                    val newModule = BroadcastModule(name)
                    modules.add(newModule)
                    newModule
                }
            }

            Connection(sourceModule, destinationModules)
        }

        modules.filterIsInstance<ConjunctionModule>()
            .forEach { conjunctionModule ->
                conjunctionModule.inputs = connections.filter { connection ->
                    connection.destinations.any { it.name == conjunctionModule.name }
                }.map { it.source }
            }

        val processor = Processor(connections)
        return processor
    }

    data class Connection(
        val source: Module,
        val destinations: List<Module>,
    ) {
        val name = source.name


        fun transmit(signal: Signal, processor: Processor) {
            val newSignal = source.process(signal)
            if (newSignal == high && source is ConjunctionModule) {
                if (processor.nearEndNodes.contains(source.name)) {
                    processor.updateConjunction(source)
                }

            }
            if (newSignal != null) {
                destinations.forEach {
//                    "${source.name} -$newSignal-> ${it.name}".println()
                    processor.updateCount(newSignal)
                    processor.send(it, newSignal)
                }
            }
        }
    }

    sealed interface Module {
        val name: String
        fun process(signal: Signal): Signal?
        val out: Signal
    }

    data class BroadcastModule(
        override val name: String,
    ) : Module {
        override val out = low
        override fun process(signal: Signal) = signal
    }

    data class FlipFlopModule(
        override val name: String,
    ) : Module {
        private var isOn: Boolean = false

        override val out: Signal
            get() = if (isOn) high else low

        override fun process(signal: Signal): Signal? {
            if (signal == low) {
                isOn = !isOn
            } else {
                return null
            }

            return out
        }
    }

    data class ConjunctionModule(
        override val name: String,
    ) : Module {
        lateinit var inputs: List<Module>

        override var out = low
        override fun process(signal: Signal) = inputs.all { it.out == high }
            .let {
                out = if (it) low else high
                out
            }
    }

    class Processor(
        private var connections: List<Connection>,
    ) {
        private val arrayDeque = ArrayDeque<Action>()
        private var failSwitch = Int.MAX_VALUE
        private val buttonTrigger =
            Connection(BroadcastModule("button"), listOf(connections.first { it.name == "broadcaster" }.source))

        private var highCount = 0L
        private var lowCount = 0L

        val nearEndNodes = listOf("bt", "dl", "fr", "rv")
        val conjuctions: MutableMap<Module, Int?> = connections.map { it.source }
            .filter { it.name in nearEndNodes }
            .associateWith { it.println(); null }
            .toMutableMap()
        var count = 0

        fun solve1(): Any {
            repeat(10000) {
                arrayDeque.addLast(Action(buttonTrigger, low))
                failSwitch = 100000
                process()
            }
            return highCount * lowCount
        }

        fun solve2(): Any {

            repeat(10000) {
                count = it
                arrayDeque.addLast(Action(buttonTrigger, low))
                failSwitch = 100000
                process()
                if (conjuctions.all { it.value != null }) {
                    return conjuctions.values.filterNotNull().map { it.toLong() + 1 }.let { lcm(it.toLongArray()) }
                        .also { println(it) }
                }
            }
            fail("No solution found")
        }

        private fun process() {
            while (arrayDeque.isNotEmpty()) {
                failSwitch--
                if (failSwitch == 0) throw IllegalStateException("Fail switch triggered")

                val action = arrayDeque.removeFirst()
                val connection = action.connection
                connection.transmit(action.signal, this)
            }
        }

        fun send(destination: Module, signal: Signal) {
            val connection = connections.firstOrNull { it.name == destination.name }
            if (null != connection) {
                arrayDeque.addLast(Action(connection, signal))
            } else {
                if (signal == low)
                    throw IllegalStateException("No connection found for ${destination.name}")
            }
        }

        fun updateCount(newSignal: Signal) {
            when (newSignal) {
                high -> highCount++
                low -> lowCount++
            }
        }

        fun updateConjunction(module: Module) {
            "${module.name} - $count".println()

            if (conjuctions[module] == null) {
                conjuctions[module] = count
            }
        }

        private class Action(
            val connection: Connection,
            val signal: Signal,
        )

    }

    enum class Signal {
        low, high,
    }
}

class DaySolvedTest : DayTest(DaySolved, true) {

    @Test
    fun testPart1() = testPart1(32000000L)

    @Test
    fun realPart1() = realPart1(834323022L)

    @Test
    fun testPart2() = testPart2("SOLVE ME")

    @Test
    fun realPart2() = realPart2("SOLVE ME")
//    225153794880000
//    938140812000
}