object Day03 : Runner<Int>(3, 157, 70) {
    override fun part1(input: List<String>): Int {
        val priorities = input.map { contents ->
            val half = contents.length / 2
            val left = contents.substring(0, half)
            val right = contents.substring(half, contents.length)
            val overlap = left.toCharArray().intersect(right.toCharArray().toSet()).single()

            overlap.priority()
        }

        return priorities.sum()
    }

    override fun part2(input: List<String>): Int {
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

    private fun Char.priority() = code - if (this.isUpperCase()) 38 else 96
}
