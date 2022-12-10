object Day01 : Runner<Int, Int>(1, 24000, 45000) {
    override fun part1(input: List<String>): Int = toCalorieMap(input).values.maxOf { it.sum() }

    override fun part2(input: List<String>): Int {
        val calories = toCalorieMap(input).values.map { it.sum() }
            .sortedByDescending { it }
            .take(3)

        return calories.sum()
    }

    private fun toCalorieMap(input: List<String>): Map<Int, List<Int>> {
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
}
