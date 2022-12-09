import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object Day09 : Runner<Int>(9, 13, 36) {
    override fun part1(input: List<String>): Int {
        val grid = Grid()

        input.forEach {
            val move = Move(it)
            grid.moveHead(move)
        }

        return grid.tailVisited.size
    }

    override fun part2(input: List<String>): Int {
        input.forEach {
            val move = Move(it)
            println(move)
        }

        return 0
    }

    class Move private constructor(
        val direction: Direction,
        val steps: Int
    ) {
        companion object {
            operator fun invoke(s: String): Move {
                val tokens = s.split(" ")

                return Move(Direction.of(tokens[0]), tokens[1].toInt())
            }
        }

        override fun toString() = "$direction $steps"
    }
    enum class Direction(val s: String) {
        LEFT("L"), RIGHT("R"), UP("U"), DOWN("D");

        companion object {
            fun of(s: String): Direction {
                return when (s.uppercase()) {
                    "L" -> LEFT
                    "R" -> RIGHT
                    "U" -> UP
                    "D" -> DOWN
                    else -> error("unknown direction")
                }
            }
        }
    }

    class Grid {
        val tailVisited: MutableSet<Point> = mutableSetOf(Point(0, 0))
        private val headVisited: MutableSet<Point> = mutableSetOf(Point(0, 0))

        var head = Point(0, 0)
        var tail = Point(0, 0)

        fun moveHead(move: Move) {
            val newHead = when (move.direction) {
                Direction.LEFT -> Point(head.x - move.steps, head.y)
                Direction.RIGHT -> Point(head.x + move.steps, head.y)
                Direction.UP -> Point(head.x, head.y - move.steps)
                Direction.DOWN -> Point(head.x, head.y + move.steps)
            }

            val headPositions = head.connectingLine(newHead)

            headPositions.forEach {
                // Move head, add to list
                head = it
                headVisited.add(head)

                // Move tail, add to list
                tail = tail.moveNear(head)
                tailVisited.add(tail)

                // Print grid
                // println(this).also { Thread.sleep(200) }
            }
        }

        override fun toString(): String {
            // Get min and max x,y
            val minX = min(tailVisited.minOf { it.x }, headVisited.minOf { it.x })
            val minY = min(tailVisited.minOf { it.y }, headVisited.minOf { it.y })
            val maxX = max(tailVisited.maxOf { it.x }, headVisited.maxOf { it.x })
            val maxY = max(tailVisited.maxOf { it.y }, headVisited.maxOf { it.y })

            val height = maxY - minY + 1
            val width = maxX - minX + 1

            val grid = List(height) { MutableList(width) { '⋅' } }

            // draw s
            grid[-minY][-minX] = 's'

            // draw T
            grid[tail.y - minY][tail.x - minX] = 'T'

            // draw H
            grid[head.y - minY][head.x - minX] = 'H'

            return grid.joinToString("\n", "\n") {
                it.joinToString("") { c -> c.toString() }
            }
        }
    }

    fun Point.connectingLine(other: Point): List<Point> {
        require(x == other.x || y == other.y) {
            "Points are not in straight line: $this -> $other"
        }
        val xRange = x toward other.x
        val yRange = y toward other.y

        return when (xRange.first) {
            xRange.last -> yRange.map { Point(xRange.first, it) }
            else -> xRange.map { Point(it, yRange.first) }
        }
    }

    fun Point.moveNear(other: Point): Point {
        val xDiff = other.x - x
        val yDiff = other.y - y

        if (abs(xDiff) <= 1 && abs(yDiff) <= 1) return this

        return when {
            xDiff == 0 -> Point(x, y + (yDiff / abs(yDiff)))
            yDiff == 0 -> Point(x + (xDiff / abs(xDiff)), y)
            else -> Point(x + (xDiff / abs(xDiff)), y + (yDiff / abs(yDiff)))
        }
    }

    infix fun Int.toward(to: Int): IntProgression {
        val step = if (this > to) -1 else 1
        return IntProgression.fromClosedRange(this, to, step)
    }

    data class Point(val x: Int, val y: Int) {
        override fun toString(): String {
            return "($x,$y)"
        }
    }
}
