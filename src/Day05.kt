object Day05 : Runner<String, String>(5, "CMZ", "MCD") {
    override fun part1(input: List<String>): String {
        val (stacks, instructions) = parseInput(input)

        // run each instruction
        instructions.forEach() {
            with(it) {
                repeat(count) { stacks[target].addFirst(stacks[source].removeFirst()) }
            }
        }

        // get top of each tower
        return stacks.joinToString("") { it.first().toString() }
    }

    override fun part2(input: List<String>): String {
        val (stacks, instructions) = parseInput(input)

        // run each instruction
        instructions.forEach() {
            with(it) {
                val tmp = ArrayDeque<Char>()
                repeat(count) { tmp.addFirst(stacks[source].removeFirst()) }
                repeat(count) { stacks[target].addFirst(tmp.removeFirst()) }
            }
        }

        // get top of each tower
        return stacks.joinToString("") { it.first().toString() }
    }

    data class Instruction(val count: Int, val source: Int, val target: Int)

    private fun String.toInstruction(): Instruction {
        val regex = """move (\d+) from (\d+) to (\d+)""".toRegex()
        val matchResult = regex.find(this)
        val (count, source, target) = matchResult!!.destructured

        return Instruction(count.toInt(), source.toInt() - 1, target.toInt() - 1)
    }

    private fun parseInput(input: List<String>): Pair<List<ArrayDeque<Char>>, List<Instruction>> {
        val blankLine = input.indexOf("")

        // Shamelessly stolen from the internet since input parsing is bleh
        val numberOfStacks = input[blankLine - 1].split(" ").filter { it.isNotBlank() }.maxOf { it.toInt() }
        val stacks = List(numberOfStacks) { ArrayDeque<Char>() }


        input.subList(0, blankLine - 1).map { line ->
            line.mapIndexed { index, char ->
                if (char.isLetter()) stacks[index / 4].addLast(line[index])
            }
        }

        val instructions = input.subList(blankLine + 1, input.size).map { it.toInstruction() }

        return stacks to instructions
    }
}
