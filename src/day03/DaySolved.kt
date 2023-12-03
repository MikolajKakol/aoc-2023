package day03

import Day
import DayTest
import coerce
import org.junit.Test
import util.Matrix2D
import util.Point2D

object DaySolved : Day {

    override fun part1(input: List<String>): Any {
        val matrix = Matrix2D.create(input)
        return matrix.findNumbers()
            .filter { it.hasSerialNumber(matrix) }
            .sumOf { it.number }
    }

    override fun part2(input: List<String>): Any {
        val matrix = Matrix2D.create(input)

        val parts = matrix.findNumbers()
            .filter { it.maybePartOfGear(matrix) }

        return parts.mapNotNull {
            val other = it.closeTo(matrix, parts - it)
            if (other != null) {
                (if (it < other) it to other else other to it)
            } else null
        }
            .toSet()
            .sumOf { it.first.number * it.second.number }
    }

}

private fun Matrix2D.findNumbers(): List<PositionedNumber> {
    return array.foldIndexed(mutableListOf()) { row, acc, chars ->

        "[0-9]*".toRegex()
            .findAll(chars.joinToString(""))
            .map {
                val startIndex = it.range.first
                val number = it.value
                if (number.isEmpty()) {
                    null
                } else {
                    val position = Point2D(startIndex, row)
                    PositionedNumber(number.toInt(), position)
                }
            }
            .filterNotNull()
            .toList()
            .let { acc.addAll(it) }

        acc
    }
}


data class PositionedNumber(
    val number: Int,
    val startPosition: Point2D,
) {

    private val length = number.toString().length - 1

    val horizontalRange: IntRange
        get() {
            val hStart = startPosition.x - 1
            val hEnd = startPosition.x + length + 1
            return hStart..hEnd
        }
    val verticalRange: IntRange
        get() {
            val vStart = startPosition.y - 1
            val vEnd = startPosition.y + 1
            return vStart..vEnd
        }

    val surroundingPositions: List<Point2D>
        get() {
            val positions = mutableListOf<Point2D>()
            verticalRange.forEach { y ->
                horizontalRange.forEach { x ->
                    positions.add(Point2D(x, y))
                }
            }
            return positions
        }

    fun contains(x: Int, y: Int) =
        (startPosition.x..startPosition.x + length).contains(x) &&
                startPosition.y == y

    fun hasSerialNumber(matrix: Matrix2D): Boolean {
        return checkSurrounding(matrix) { !it.isDigit() && it != '.' }
    }

    fun maybePartOfGear(matrix: Matrix2D): Boolean {
        return checkSurrounding(matrix) { it == '*' }
    }

    private fun checkSurrounding(matrix: Matrix2D, check: (Char) -> Boolean): Boolean {
        val verticalRange = verticalRange.coerce(0, matrix.height - 1)
        val horizontalRange = horizontalRange.coerce(0, matrix.width - 1)

        return verticalRange.any { y ->
            horizontalRange.any { x ->
                check(matrix.array[y][x])
            }
        }
    }

    fun closeTo(matrix: Matrix2D, parts: List<PositionedNumber>): PositionedNumber? {
        return parts
            .filter { other ->
                other.verticalRange.intersect(verticalRange).isNotEmpty() &&
                        other.horizontalRange.intersect(horizontalRange).isNotEmpty()
            }
            .firstOrNull { other ->
                surroundingPositions.intersect(other.surroundingPositions).any {
                    matrix.getOrNull(it.x, it.y) == '*'
                }
            }
    }

    operator fun compareTo(other: PositionedNumber): Int {
        return compareBy<PositionedNumber>(
            { it.number },
            { it.startPosition.x },
            { it.startPosition.y }
        )
            .compare(this, other)
    }
}

class DaySolvedTest : DayTest(DaySolved) {

    @Test
    fun testPart1() = testPart1(4361)

    @Test
    fun realPart1() = realPart1(527369)

    @Test
    fun testPart2() = testPart2(467835)

    @Test
    fun realPart2() = realPart2(73074886)
}