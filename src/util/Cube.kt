package util

import kotlin.math.absoluteValue

data class Cube(val start: Point3D, val end: Point3D, val name: String? = null) {

    val charName = (name ?: "").hashCode().toChar()

    val with: Int = (end.x - start.x).absoluteValue
    val height: Int = (end.y - start.y).absoluteValue
    val depth: Int = (end.z - start.z).absoluteValue

    val zPlaceRange = (start.x..end.x) to (start.y..end.y)
    val zPlanePoints: Sequence<Point2D>
        get() = zPlaceRange
            .let { (xRange, yRange) ->
                sequence {
                    xRange.flatMap { x ->
                        yRange.map { y ->
                            yield(Point2D(x, y))
                        }
                    }
                }
            }

    fun translate(x: Int = 0, y: Int = 0, z: Int = 0) = copy(
        start = start.translate(x, y, z),
        end = end.translate(x, y, z),
    )

    fun traverse(action: (x: Int, y: Int, z: Int, c: Char) -> Unit) {
        (start.z..end.z).forEach { z ->
            (start.y..end.y).forEach { y ->
                (start.x..end.x).forEach { x ->
                    action(x, y, z, charName)
                }
            }
        }
    }

    override fun toString(): String {
        val name = name ?: ""
        return "$name(${start.x},${start.y},${start.z})~(${end.x},${end.y},${end.z})$charName"
    }
}