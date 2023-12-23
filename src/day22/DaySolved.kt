package day22

import Day
import DayTest
import day22.DaySolved.above
import org.junit.Test
import util.Cube
import util.Matrix3D
import util.Point3D

object DaySolved : Day {

    override suspend fun part1(input: List<String>) = prepareCubes(input)
        .let { cubes ->
            cubes.filter {
                val above = cubes.above(it)
                canRemove(it, above, cubes)
            }
                .size
        }

    override suspend fun part2(input: List<String>) =
        prepareCubes(input)
            .let { cubes ->
                val cubesMap = cubes.associateBy { it.name }
                val multiFallCubes = cubes.filter {
                    val above = cubes.above(it)
                    !canRemove(it, above, cubes)
                }
                val maxX = cubes.maxOf { it.end.x }
                val maxY = cubes.maxOf { it.end.y }
                val maxZ = cubes.maxOf { it.end.z }

                multiFallCubes
                    .sumOf { removeCube ->
                        val matrix = Matrix3D.create(maxX + 1, maxY + 1, maxZ + 1) { _, _, z ->
                            if (z == 0) {
                                '-'
                            } else {
                                '.'
                            }
                        }

                        (cubes - removeCube)
                            .map { it.settle(matrix) }
                            .map {  cube ->
                                if (cubesMap[cube.name]!!.start.z == cube.start.z) {
                                    0
                                } else {
                                    1
                                }
                            }.sum()
                    }
            }


    private fun prepareCubes(input: List<String>): List<Cube> {
        return input
            .mapIndexed { index, line -> line.cube(index) }
            .sortedBy { it.start.z }
            .let { cubes ->
                val maxX = cubes.maxOf { it.end.x }
                val maxY = cubes.maxOf { it.end.y }
                val maxZ = cubes.maxOf { it.end.z }

                val matrix = Matrix3D.create(maxX + 1, maxY + 1, maxZ + 1) { _, _, z ->
                    if (z == 0) {
                        '-'
                    } else {
                        '.'
                    }
                }

                cubes.map { it.settle(matrix) }
            }
    }


    private fun canRemove(cube: Cube, above: List<Cube>, cubes: List<Cube>): Boolean {
        if (above.isEmpty()) {
            return true
        }
        val isSingle = above.any {
            val below = cubes.below(it)
            below.size == 1 && below.first() == cube
        }
        return !isSingle

    }

    private fun String.cube(index: Int) =
        split("~")
            .let { Cube(it[0].point(), it[1].point(), "c$index") }


    private fun String.point() = split(",")
        .map { it.toInt() }
        .let { Point3D(it[0], it[1], it[2]) }

    private fun Cube.settle(matrix: Matrix3D): Cube {
        var offset = 0
        while (true) {
            offset++
            val nextZ = start.z - offset
            val moveDown = zPlanePoints
                .all { point ->
                    matrix.get(point.x, point.y, nextZ) == '.'
                }
            if (!moveDown) {
                offset--
                val cube = if (offset == 0) this
                else translate(z = -offset)
                cube.writeTo(matrix)
                return cube
            }

        }
    }

    private fun Cube.writeTo(matrix: Matrix3D) {
        traverse { x, y, z, c ->
            matrix.set(x, y, z, '#')
        }
    }

    private fun Iterable<Cube>.above(cube: Cube): List<Cube> {
        val cubePoints = cube.zPlanePoints.toList()
        return filter { other ->
            if (other.start.z == cube.end.z + 1) {
                other.zPlanePoints.any { cubePoints.contains(it) }
            } else {
                false
            }
        }
    }

    private fun Iterable<Cube>.below(cube: Cube): List<Cube> {
        val cubePoints = cube.zPlanePoints.toList()
        return filter { other ->
            if (other.end.z == cube.start.z - 1) {
                other.zPlanePoints.any { cubePoints.contains(it) }
            } else {
                false
            }
        }

    }
}

class DaySolvedTest : DayTest(DaySolved, true) {

    @Test
    fun testPart1() = testPart1(5)

    @Test
    fun realPart1() = realPart1(437)

    @Test
    fun testPart2() = testPart2(7)

    @Test
    fun realPart2() = realPart2(42561)
}