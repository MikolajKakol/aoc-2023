package util

data class Point2D(val x: Int, val y: Int){

    fun move(direction: Direction): Point2D {
        return when (direction) {
            Direction.NORTH -> Point2D(x, y - 1)
            Direction.SOUTH -> Point2D(x, y + 1)
            Direction.EAST -> Point2D(x + 1, y)
            Direction.WEST -> Point2D(x - 1, y)
        }
    }

    override fun toString(): String {
        return "($x, $y)"
    }
}

enum class Direction {
    NORTH, SOUTH, EAST, WEST
}
