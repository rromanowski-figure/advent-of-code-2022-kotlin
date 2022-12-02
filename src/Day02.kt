fun main() {
    fun part1(input: List<String>): Int {
        val results = input.map {
            val values = it.split(" ")
            val them = Shape.them(values[0])
            val me = Shape.me(values[1])

            // println("$them v. $me - ${me.resultAgainst(them)} - ${me.vs(them)}")

            me.vs(them)
        }

        return results.sum()
    }

    fun part2(input: List<String>): Int {
        val results = input.map {
            val values = it.split(" ")
            val them = Shape.them(values[0])
            val intendedResult = RpsResult.of(values[1])
            val me = when (intendedResult) {
                RpsResult.Win -> them.loses
                RpsResult.Tie -> them
                RpsResult.Loss -> them.beats
            }

            // println("$them - $intendedResult - $me - ${intendedResult.points + me.points}")
            intendedResult.points + me.points
        }

        return results.sum()
    }

    val sampleInput = readInput("input-02-sample")
    val sampleOutput1 = part1(sampleInput)
    check(sampleOutput1 == 15)

    val sampleOutput2 = part2(sampleInput)
    check(sampleOutput2 == 12)

    val input = readInput("input-02")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

sealed class Shape(val points: Int) {
    companion object {
        fun them(s: String) = when (s) {
            "A" -> Rock
            "B" -> Paper
            "C" -> Scissors
            else -> error("Invalid input: $s")
        }

        fun me(s: String) = when (s) {
            "X" -> Rock
            "Y" -> Paper
            "Z" -> Scissors
            else -> error("Invalid input: $s")
        }
    }

    abstract val beats: Shape
    abstract val loses: Shape

    fun resultAgainst(other: Shape): RpsResult {
        return when {
            this.beats == other -> RpsResult.Win
            other.beats == this -> RpsResult.Loss
            else -> RpsResult.Tie
        }
    }

    fun vs(other: Shape): Int = this.resultAgainst(other).points + this.points

    override fun toString(): String = this::class.simpleName ?: "Unknown Shape"
}

object Rock : Shape(1) {
    override val beats: Shape
        get() = Scissors
    override val loses: Shape
        get() = Paper
}
object Paper : Shape(2) {
    override val beats: Shape
        get() = Rock
    override val loses: Shape
        get() = Scissors
}
object Scissors : Shape(3) {
    override val beats: Shape
        get() = Paper
    override val loses: Shape
        get() = Rock
}

enum class RpsResult(val points: Int) {
    Win(6),
    Tie(3),
    Loss(0);

    companion object {
        fun of(s: String) = when (s) {
            "X" -> Loss
            "Y" -> Tie
            "Z" -> Win
            else -> error("Invalid input: $s")
        }
    }
}
