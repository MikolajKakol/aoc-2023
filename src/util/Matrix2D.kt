package util

class Matrix2D(
    val array: Array<CharArray> = emptyArray(),
) {

    val width: Int
        get() = array[0].size

    val height: Int
        get() = array.size

    fun traverse(action: (x: Int, y: Int, c: Char) -> Unit) {
        array.forEachIndexed { y, chars ->
            chars.forEachIndexed { x, char ->
                action(x, y, char)
            }
        }
    }

    fun get(point: Point2D): Char {
        return array[point.y][point.x]
    }
    fun get(x: Int, y: Int): Char {
        return array[y][x]
    }

    fun getOrNull(point: Point2D): Char? {
        return getOrNull(point.x, point.y)
    }

    fun getOrNull(x: Int, y: Int): Char? {
        return if (x in 0 until width && y in 0 until height) {
            array[y][x]
        } else {
            null
        }
    }

    fun find(matcher: (value: Char) -> Boolean): Point2D {
        for ((y, row) in array.withIndex()) {
            for ((x, item) in row.withIndex()) {
                if (matcher(item)) return Point2D(x, y)
            }
        }

        throw NoSuchElementException()
    }

    fun print(){
        array.forEach { println(it) }
    }

    companion object {
        fun create(input: List<String>): Matrix2D {
            val twoDArray = Array(input.size) { i -> input[i].toCharArray() }
            return Matrix2D(twoDArray)
        }
    }
}
