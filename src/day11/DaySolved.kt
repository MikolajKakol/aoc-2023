package day11

import Day
import DayTest
import org.junit.Test
import util.Matrix2D
import util.Point2D
import kotlin.test.BeforeTest

class DaySolved(val growthRate: Int) : Day {

    override fun part1(input: List<String>) = input
        .let { Universe(it, growthRate).solve() }

    override fun part2(input: List<String>) = input
        .let { Universe(it, growthRate).solve() }

    class Universe(input: List<String>, val growthRate: Int) {
        private val matrix = Matrix2D.create(input)

        fun solve(): Any {
            val verticalSpaceExpansions = getVerticalSpaceExpansions()
            val horizontalSpaceExpansions = getHorizontalSpaceExpansions()

            val galaxies = getGalaxies()
            val connections = galaxies
                .flatMapIndexed { index, point2D -> galaxies.drop(index + 1).map { point2D to it } }

            return connections
                .sumOf { connection ->
                    val a = connection.first.position
                    val b = connection.second.position
                    val distance = a.manhattanDistance(b)
                    val horizontal = (a.x..b.x).intersect(horizontalSpaceExpansions)
                    val horizontal2 = (b.x..a.x).intersect(horizontalSpaceExpansions)
                    val vertical = (a.y..b.y).intersect(verticalSpaceExpansions)
                    val vertical2 = (b.y..a.y).intersect(verticalSpaceExpansions)
                    val growth = horizontal.size + vertical.size + horizontal2.size + vertical2.size

                    var count = distance.toLong() + growth.toLong() * growthRate.toLong()
                    if (growthRate > 1) {
                        count -= growth
                    }

                    count
                }
        }

        private fun getVerticalSpaceExpansions() = matrix.array
            .mapIndexedNotNull { y, it -> if (it.all { it == '.' }) y else null }
            .toSet()

        private fun getHorizontalSpaceExpansions() = (0 until matrix.width)
            .mapNotNull { x -> if ((0 until matrix.height).all { y -> matrix.get(x, y) == '.' }) x else null }
            .toSet()

        private fun getGalaxies() = matrix.findAll { it == '#' }
            .mapIndexed { index, point2D -> Galaxy(index + 1, point2D) }
    }

    data class Galaxy(val no: Int, val position: Point2D)
}

class DaySolvedTest : DayTest(DaySolved(1), true) {

    @BeforeTest
    fun before() {
        day = DaySolved(1)
    }

    @Test
    fun testPart1() = testPart1(374L)

    @Test
    fun realPart1() = realPart1(9556896L)

    @Test
    fun testPart2_10() {
        day = DaySolved(10)
        testPart2(1030L)
    }

    @Test
    fun testPart2_100() {
        day = DaySolved(100)
        testPart2(8410L)
    }

    @Test
    fun realPart2() {
        day = DaySolved(1000000)
        realPart2(685038186836L)
    }
}