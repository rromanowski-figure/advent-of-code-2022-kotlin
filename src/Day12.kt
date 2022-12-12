object Day12 : Runner<Int, Int>(12, 31, 29) {

    private fun grid(input: List<String>): Grid<Square> {
        val width = input.first().toCharArray().size
        val height = input.size
        val grid = Grid<Square>(height, width)

        input.forEachIndexed { y, s ->
            s.toCharArray().forEachIndexed { x, c ->
                grid.set(x, y, Square.of(c))
            }
        }

        return grid
    }

    override fun part1(input: List<String>): Int {
        val grid = grid(input)

        traverse(grid, grid.indexOf(Square.of('E')))

        println(grid)

        val end = grid.indexOf { label == 'S' }

        return grid.get(end.first, end.second)!!.stepsToVisit
    }

    private fun traverse(grid: Grid<Square>, start: Pair<Int, Int>) {
        val curPost = grid.get(start.first, start.second)!!
        grid.neighbors(start.first, start.second).filter {
            it.second.stepsToVisit > curPost.stepsToVisit + 1
        }.forEach { (p, next) ->
            if (next.elevation >= curPost.elevation - 1) {
                next.stepsToVisit = curPost.stepsToVisit + 1
                traverse(grid, p.first to p.second)
            }
        }
    }

    override fun part2(input: List<String>): Int {
        val grid = grid(input)

        traverse(grid, grid.indexOf(Square.of('E')))

        println(grid.grid.flatten().filterNotNull().filter { it.elevation == 1 })

        return grid.grid.flatten().filterNotNull().filter { it.elevation == 1 }.minOf { it.stepsToVisit }
    }

    data class Grid<T>(
        val height: Int,
        val width: Int,
    ) {
        val grid: List<MutableList<T?>> = List(height) { MutableList(width) { null } }

        fun indexOf(t: T): Pair<Int, Int> {
            grid.flatMapIndexed { y, row ->
                row.mapIndexed { x, cell ->
                    if (cell == t) return x to y
                }
            }

            return -1 to -1
        }
        fun indexOf(lambda: T.() -> Boolean): Pair<Int, Int> {
            grid.flatMapIndexed { y, row ->
                row.mapIndexed { x, cell ->
                    if (cell?.lambda() == true) return x to y
                }
            }

            return -1 to -1
        }
        fun get(x: Int, y: Int) = grid[y][x]
        fun set(x: Int, y: Int, t: T) {
            grid[y][x] = t
        }
        fun column(x: Int) = grid.map { it[x] }
        fun row(y: Int) = grid[y]
        fun neighbors(x: Int, y: Int): List<Pair<Pair<Int, Int>, T>> {
            return listOf(
                (x to y - 1),
                (x - 1 to y),
                (x to y + 1),
                (x + 1 to y),
            ).map {
                it to grid.getOrNull(it.second)?.getOrNull(it.first)
            }.filter { it.second != null }.filterIsInstance<Pair<Pair<Int, Int>, T>>()
        }

        override fun toString(): String {
            return grid.joinToString("\n") { it.joinToString(" ") }
        }
    }

    data class Square(
        val label: Char,
        val elevation: Int,
        var stepsToVisit: Int = Int.MAX_VALUE,
    ) {
        companion object {
            fun of(c: Char): Square {
                if (c == 'S') return Square(c, 1)
                if (c == 'E') return Square(c, 26, 0)

                return Square(c, c.code - 'a'.code + 1)
            }
        }

        override fun toString(): String {
            return "${
            elevation.toString().padStart(2, '0')
            }-${
            if (stepsToVisit == Int.MAX_VALUE) "na"
            else stepsToVisit.toString().padStart(2, '0')
            }"
        }
    }
}
