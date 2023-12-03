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

    fun getOrNull(x: Int, y: Int): Char? {
        return if (x in 0 until width && y in 0 until height) {
            array[y][x]
        } else {
            null
        }
    }

    companion object {
        fun create(input: List<String>): Matrix2D {
            val twoDArray = Array(input.size) { i -> input[i].toCharArray() }
            return Matrix2D(twoDArray)
        }
    }
}
