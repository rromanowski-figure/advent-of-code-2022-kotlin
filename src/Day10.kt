object Day10 : Runner<Int, String>(10, 13140, "") {
    override fun part1(input: List<String>): Int {
        val states = mutableListOf(State(0, 0, 1, 1))

        input.forEach { s -> states.add(states.lastOrNull()!!.next(Instruction.of(s))) }

        val output = generateSequence(20) { it + 40 }.takeWhile { it <= 220 }.map { i ->
            i to states.first { i in it.startCycle..it.endCycle }
        }

        return output.sumOf { it.first * it.second.valueDuring }
    }

    override fun part2(input: List<String>): String {
        val states = mutableListOf(State(0, 0, 1, 1))

        input.forEach { s ->
            val instruction = Instruction.of(s)
            val state = states.lastOrNull()!!.next(instruction)
            states.add(state)
        }

        val screen = CRT()

        for (cycle in 1..(screen.height * screen.width)) {
            val row = (cycle - 1) / screen.width
            val column = (cycle - 1) mod screen.width
            screen.append(
                row,
                states.first { cycle in it.startCycle..it.endCycle }.sprite(
                    screen.width,
                    " ",
                    "@",
                )[column]
            )
        }

        println(screen)

        return ""
    }

    class CRT(val height: Int = 6, val width: Int = 40) {
        var screen = MutableList(height) { "" }

        fun append(x: Int, c: Char) {
            screen[x] += c.toString()
        }

        override fun toString() = screen.joinToString("\n")
    }

    data class State(
        val startCycle: Int,
        val endCycle: Int,
        val valueDuring: Int,
        val valueAfter: Int,
    ) {
        fun next(i: Instruction): State {
            return State(
                endCycle + 1,
                endCycle + i.cycles,
                valueAfter,
                valueAfter + when (i) {
                    is Add -> i.x
                    Noop -> 0
                }
            )
        }

        fun sprite(width: Int = 40, dark: String = ".", light: String = "#"): String {
            val prefixSize = Integer.min(width, Integer.max(0, valueDuring - 1))
            val spriteSize = Integer.max(0, (Integer.min(40, valueDuring + 2) - Integer.max(0, valueDuring - 1)))
            val suffixSize = Integer.min(width, (width - Integer.min(valueDuring + 2, width)))

            return dark.repeat(prefixSize) +
                light.repeat(spriteSize) +
                dark.repeat(suffixSize)
        }
    }

    sealed interface Instruction {
        val cycles: Int

        companion object {
            fun of(s: String): Instruction {
                return with(s.split(" ")) {
                    when (this[0]) {
                        "noop" -> Noop
                        "addx" -> Add(this[1].toInt())
                        else -> error("unknown instruction")
                    }
                }
            }
        }
    }
    object Noop : Instruction {
        override val cycles: Int get() = 1
        override fun toString() = "Noop"
    }
    data class Add(val x: Int) : Instruction { override val cycles: Int get() = 2 }

    infix fun Int.mod(other: Int) = this % other
}
