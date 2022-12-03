fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val sampleInput = readInput("input-00-sample")
    val sampleOutput1 = part1(sampleInput)
    check(sampleOutput1 == 0)

    val sampleOutput2 = part2(sampleInput)
    check(sampleOutput2 == 0)

    val input = readInput("input-00")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
