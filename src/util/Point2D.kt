package util

import kotlin.math.abs

data class Point2D(val x: Int, val y: Int) {

    fun move(direction: Direction, distance: Int = 1): Point2D {
        return when (direction) {
            Direction.NORTH -> Point2D(x, y - distance)
            Direction.SOUTH -> Point2D(x, y + distance)
            Direction.EAST -> Point2D(x + distance, y)
            Direction.WEST -> Point2D(x - distance, y)
        }
    }

    infix fun with(direction: Direction): DirectedPoint2D {
        return DirectedPoint2D(this, direction)
    }

    fun manhattanDistance(b: Point2D): Int {
        return abs(x - b.x) + abs(y - b.y)
    }

    override fun toString(): String {
        return "($x, $y)"
    }
}

data class DirectedPoint2D(val point: Point2D, val direction: Direction) {

    fun move(direction: Direction) = point.move(direction) with direction

    override fun toString(): String {
        return "$point $direction"
    }
}

enum class Direction {
    NORTH, SOUTH, EAST, WEST;

    val isHorizontal: Boolean
        get() = this == EAST || this == WEST

    val isVertical: Boolean
        get() = this == NORTH || this == SOUTH
}
