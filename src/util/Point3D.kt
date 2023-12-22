package util

data class Point3D(val x: Int, val y: Int, val z: Int) {

    override fun toString(): String {
        return "($x, $y, $z)"
    }

    fun translate(x: Int, y: Int, z: Int): Point3D {
        return copy(
            x = this.x + x,
            y = this.y + y,
            z = this.z + z,
        )
    }
}