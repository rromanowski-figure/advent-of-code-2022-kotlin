import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Convert day (1) to padded string (01)
 */
fun Int.dayString(): String = toString().padStart(2, '0')

/**
 * Reads lines from the given input file.
 */
fun readInput(name: Int) = File("src/resources", "input-${name.dayString()}")
    .readLines()

/**
 * Reads lines from the given sample input file.
 */
fun readSampleInput(name: Int) = File("src/resources", "input-${name.dayString()}-sample")
    .readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')
