fun main() {
    fun part1(input: List<String>): Int {
        var lastCut = 0
        val calorieMap = mutableMapOf<Int, List<Int>>()

        input.foldIndexed(calorieMap) { index, map, calories ->
            if (calories.isNullOrBlank()) {
                map[lastCut] = input.subList(lastCut, index).map { it.toInt() }
                lastCut = index + 1
            }

            map
        }

        return calorieMap.values.maxOf { it.sum() }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val sampleInput = readInput("input-01.1-sample")
    check(part1(sampleInput) == 24000)

    val input = readInput("input-01.1")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
