package util

class Matrix3D(private val array: Array<Array<CharArray>> = emptyArray()) {

    val xSize: Int
        get() = array[0][0].size

    val ySize: Int
        get() = array[0].size

    val zSize: Int
        get() = array.size

//    val rows: List<Array<CharArray>>
//        get() = array.toList()
//
//    val columns: List<Array<CharArray>>
//        get() = (0 until xSize)
//            .map { x ->
//                Array(ySize) { y ->
//                    CharArray(zSize) { z ->
//                        get(x, y, z)
//                    }
//                }
//            }

    fun traverse(action: (x: Int, y: Int, z: Int, c: Char) -> Unit) {
        array.forEachIndexed { z, arrays ->
            arrays.forEachIndexed { y, chars ->
                chars.forEachIndexed { x, char ->
                    action(x, y, z, char)
                }
            }
        }
    }

    fun get(point: Point3D): Char {
        return array[point.z][point.y][point.x]
    }

    fun get(x: Int, y: Int, z: Int): Char {
        return array[z][y][x]
    }

    fun getOrNull(point: Point3D): Char? {
        return getOrNull(point.x, point.y, point.z)
    }

    fun getOrNull(x: Int, y: Int, z: Int): Char? {
        return array.getOrNull(z)?.getOrNull(y)?.getOrNull(x)
    }

    fun set(point: Point3D, value: Char) {
        array[point.z][point.y][point.x] = value
    }

    fun set(x: Int, y: Int, z: Int, value: Char) {
        array[z][y][x] = value
    }

    fun setAll(value: Char) {
        traverse { x, y, z, _ ->
            set(x, y, z, value)
        }
    }

    companion object {
        fun create(width: Int, height: Int, depth: Int, init: (x: Int, y: Int, z: Int) -> Char): Matrix3D {
            return Matrix3D(Array(depth) { z ->
                Array(height) { y ->
                    CharArray(width) { x ->
                        init(x, y, z)
                    }
                }
            })
        }
    }
}