sealed class Runner<T, V>(val day: Int, private val check1: T, private val check2: V) {
    abstract fun part1(input: List<String>): T
    abstract fun part2(input: List<String>): V

    fun run() {
        println("\n== Sample 1 ==")
        val sampleOutput1 = part1(readSampleInput(day, 1))
        check(sampleOutput1 == check1) {
            "Sample input for part 1 is wrong. Expected: $check1, Actual: $sampleOutput1"
        }

        val input = readInput(day)
        println("\n== Part 1 ==")
        println("Part 1:\n${part1(input)}")

        println("\n== Sample 2 ==")
        val sampleOutput2 = part2(readSampleInput(day, 2))
        check(sampleOutput2 == check2) {
            "Sample input for part 2 is wrong. Expected: $check2, Actual: $sampleOutput2"
        }

        println("\n== Part 2 ==")
        println("Part 2:\n${part2(input)}")
    }
    companion object {
        fun of(day: Int): Runner<*, *> {
            val runners: Map<Int, Runner<*, *>> = listOf(
                Day01,
                Day02,
                Day03,
                Day04,
                Day05,
                Day06,
                Day07,
                Day08,
                Day09,
                Day10,
                Day11,
                Day12,
            ).associateBy { it.day }

            return runners[day] ?: error("No runner for day $day")
        }
    }
}
