sealed class Runner<T>(private val day: Int, private val check1: T, private val check2: T) {
    abstract fun part1(input: List<String>): T
    abstract fun part2(input: List<String>): T

    fun run() {
        val sampleOutput1 = part1(readSampleInput(day, 1))
        check(sampleOutput1 == check1) {
            "Sample input for part 1 is wrong. Expected: $check1, Actual: $sampleOutput1"
        }

        val input = readInput(day)
        println("Part 1: ${part1(input)}")

        val sampleOutput2 = part2(readSampleInput(day, 2))
        check(sampleOutput2 == check2) {
            "Sample input for part 2 is wrong. Expected: $check2, Actual: $sampleOutput2"
        }

        println("Part 2: ${part2(input)}")
    }
    companion object {
        fun of(day: Int): Runner<*> {
            val className = "Day${day.dayString()}"

            return Class.forName(className).kotlin.objectInstance as Runner<*>
        }
    }
}
