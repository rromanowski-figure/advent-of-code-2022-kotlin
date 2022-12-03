fun main() {
    fun Char.priority() = code - if (this.isUpperCase()) 38 else 96

    fun part1(input: List<String>): Int {
        val priorities = input.map { contents ->
            val half = contents.length / 2
            val left = contents.substring(0, half)
            val right = contents.substring(half, contents.length)
            val overlap = left.toCharArray().intersect(right.toCharArray().toSet()).single()

            overlap.priority()
        }

        return priorities.sum()
    }

    fun part2(input: List<String>): Int {
        val groups = input.chunked(3)
        val priorities = groups.map {
            val overlap = it[0].toCharArray()
                .intersect(it[1].toCharArray().toSet())
                .intersect(it[2].toCharArray().toSet())
                .single()

            overlap.priority()
        }

        return priorities.sum()
    }

    val sampleInput = readInput("input-03-sample")
    val sampleOutput1 = part1(sampleInput)
    check(sampleOutput1 == 157)

    val sampleOutput2 = part2(sampleInput)
    check(sampleOutput2 == 70)

    val input = readInput("input-03")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
