object Day04 : Runner<Int, Int>(4, 2, 4) {
    override fun part1(input: List<String>): Int {
        return input.count {
            val ranges = it.split(",")
            val section1 = ranges[0].toSection()
            val section2 = ranges[1].toSection()

            (section1.first >= section2.first && section1.last <= section2.last) ||
                (section2.first >= section1.first && section2.last <= section1.last)
        }
    }

    override fun part2(input: List<String>): Int {
        return input.count {
            val ranges = it.split(",")
            val section1 = ranges[0].toSection()
            val section2 = ranges[1].toSection()

            section1.intersect(section2).isNotEmpty()
        }
    }

    private fun String.toSection(): IntRange {
        val bounds = split("-")
        return bounds[0].toInt()..bounds[1].toInt()
    }
}
