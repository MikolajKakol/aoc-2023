import kotlin.test.assertEquals

abstract class DayTest(private val day: Day) {

    protected fun testPart1(expected: Any) {
        val actual = day.part1(read("test1"))
        assertEquals(expected, actual)
    }

    protected fun testPart2(expected: Any) {
        val actual = day.part2(read("test2"))
        assertEquals(expected, actual)
    }

    protected fun realPart1(expected: Any) {
        val actual = day.part1(read("real1"))
        assertEquals(expected, actual)
    }

    protected fun realPart2(expected: Any) {
        val actual = day.part2(read("real2"))
        assertEquals(expected, actual)
    }


    private fun read(part: String): List<String> {
        val qualifiedName = day::class.qualifiedName
        val folder = qualifiedName!!.split(".")[0]
        return readInput("$folder/$part")
    }
}