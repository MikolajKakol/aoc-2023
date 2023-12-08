import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
        .toString(16)
        .padStart(32, '0')

fun IntRange.coerce(start: Int, end: Int): IntRange {
        return start.coerceAtLeast(first)..end.coerceAtMost(last)
}

fun gcd(a: Long, b: Long): Long {
        var a = a
        var b = b
        while (b > 0) {
                val temp = b
                b = a % b // % is remainder
                a = temp
        }
        return a
}

fun gcd(input: LongArray): Long {
        var result = input[0]
        for (i in 1 until input.size) result = gcd(result, input[i])
        return result
}

fun lcm(a: Long, b: Long): Long {
        return a * (b / gcd(a, b))
}

fun lcm(input: LongArray): Long {
        var result = input[0]
        for (i in 1 until input.size) result = lcm(result, input[i])
        return result
}