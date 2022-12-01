fun main() {
    fun toCalorieMap(input: List<String>): Map<Int, List<Int>> {
        var lastCut = 0
        val calorieMap = mutableMapOf<Int, List<Int>>()

        input.foldIndexed(calorieMap) { index, map, calories ->
            if (calories.isBlank() || index == input.size - 1) {
                map[lastCut] = input.subList(lastCut, index + 1).filter { it.isNotBlank() }.map { it.toInt() }
                lastCut = index + 1
            }

            map
        }

        return calorieMap.toMap()
    }

    fun part1(input: List<String>): Int {
        return toCalorieMap(input).values.maxOf { it.sum() }
    }

    fun part2(input: List<String>): Int {
        val calories = toCalorieMap(input).values.map { it.sum() }
            .sortedByDescending { it }
            .take(3)

        println("Top 3: $calories")

        return calories.sum()
    }

    val sampleInput = readInput("input-01.1-sample")
    val sampleOutput1 = part1(sampleInput)
    check(sampleOutput1 == 24000)

    val sampleOutput2 = part2(sampleInput)
    check(sampleOutput2 == 45000)

    val input = readInput("input-01.1")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
