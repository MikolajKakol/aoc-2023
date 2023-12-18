import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals

abstract class DayTest(var day: Day, private val hasSameInputBetweenDays: Boolean = false) {

    protected fun testPart1(expected: Any) {
        solvePart1(expected, "test1")
    }

    protected fun testPart2(expected: Any) {
        solvePart2(expected, if (hasSameInputBetweenDays) "test1" else "test2")
    }

    protected fun realPart1(expected: Any) {
        solvePart1(expected, "real1")
    }

    protected fun realPart2(expected: Any) {
        solvePart2(expected, if (hasSameInputBetweenDays) "real1" else "real2")
    }

    protected fun solvePart1(expected: Any, part: String) = runTest {
        val actual = day.part1(read(part))
        assertEquals(expected, actual)
    }

    protected fun solvePart2(expected: Any, part: String) = runTest {
        val actual = day.part2(read(part))
        assertEquals(expected, actual)
    }

    private fun read(part: String): List<String> {
        val qualifiedName = day::class.qualifiedName
        val folder = qualifiedName!!.split(".")[0]
        return readInput("$folder/$part")
    }
}