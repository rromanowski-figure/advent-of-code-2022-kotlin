object Day06 : Runner<Int, Int>(6, 7, 19) {
    override fun part1(input: List<String>) = uniqueWindex(input.first(), 4)

    override fun part2(input: List<String>) = uniqueWindex(input.first(), 14)

    private fun uniqueWindex(signal: String, size: Int): Int {
        return signal.windowed(size)
            .indexOfFirst { it.toSet().size == size } + size
    }
}
