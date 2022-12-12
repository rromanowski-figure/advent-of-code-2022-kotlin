object Day11 : Runner<Long, Long>(11, 10605, -1) {

    private fun String.toItems(): List<Item> = split(",").map { Item(it.trim().toLong()) }

    object Regex {
        val monkey = """Monkey (\d+):""".toRegex()
        val itemList = """\s+Starting items: (.*)""".toRegex()
        val operation = """\s+Operation: new = (.*)""".toRegex()
        val divisibleTest = """\s+Test: divisible by (\d+)""".toRegex()
        val ifTrue = """\s+If true: throw to monkey (\d+)""".toRegex()
        val ifFalse = """\s+If false: throw to monkey (\d+)""".toRegex()
    }

    override fun part1(input: List<String>): Long {
        val monkeyMap = mutableMapOf<Long, Monkey>()
        var currentMonkey = monkey { }

        input.forEach {
            if (it.trim().isBlank()) {
                val monkey = currentMonkey.finalize()
                monkeyMap[monkey.id] = monkey
                currentMonkey = monkey { }
            }

            Regex.monkey.find(it)?.destructured?.also {
                currentMonkey.apply { id { it.component1().toLong() } }
            }

            Regex.itemList.find(it)?.destructured?.also {
                currentMonkey.apply { items { it.component1().toItems() } }
            }

            Regex.operation.find(it)?.destructured?.also {
                currentMonkey.apply {
                    operation {
                        Operation.of(it.component1()).transform
                    }
                }
            }

            Regex.divisibleTest.find(it)?.destructured?.also {
                currentMonkey.apply { divisibleBy { it.component1().toLong() } }
            }

            Regex.ifTrue.find(it)?.destructured?.also {
                currentMonkey.apply { onTrueTarget { it.component1().toLong() } }
            }

            Regex.ifFalse.find(it)?.destructured?.also {
                currentMonkey.apply { onFalseTarget { it.component1().toLong() } }
            }
        }

        val monkey = currentMonkey.finalize()
        monkeyMap[monkey.id] = monkey

        println(monkeyMap.values.joinToString("\n") { "${it.id} ${it.items}" })

        repeat(20) {
            monkeyMap.entries.sortedBy { it.key }.forEach { (_, monkey) ->
                while (monkey.items.isNotEmpty()) {
                    val item = monkey.items.removeFirst()
                    monkey.throwItem(item, monkeyMap[monkey.inspect(item)]!!)
                }
            }
        }

        monkeyMap.values.forEach {
            println("Monkey ${it.id} inspected items ${it.numberOfInspections} times.")
        }

        return monkeyMap.values
            .sortedByDescending { it.numberOfInspections }
            .take(2)
            .fold(1) { a, m -> a * m.numberOfInspections }
    }

    override fun part2(input: List<String>): Long {
        TODO("Not yet implemented")
    }

    data class Item(var worryLevel: Long) {
        override fun toString(): String {
            return worryLevel.toString()
        }
    }
    data class Monkey(
        val id: Long,
        val items: MutableList<Item>,
        val operation: Long.() -> Long,
        val divisibleBy: Long,
        val onTrueTarget: Long,
        val onFalseTarget: Long,
        var numberOfInspections: Long = 0,
    ) {
        fun inspect(item: Item): Long {
            numberOfInspections++

            item.worryLevel = item.worryLevel.operation()
            item.worryLevel /= 3

            return if (item.worryLevel % divisibleBy == 0L) onTrueTarget
            else onFalseTarget
        }

        fun throwItem(item: Item, monkey: Monkey) {
            monkey.items.add(item)
        }
    }

    class MonkeyBuilder(
        private var id: Long = -1,
        private var items: MutableList<Item> = mutableListOf(),
        private var operation: Long.() -> Long = { this },
        private var divisibleBy: Long = 0,
        private var onTrueTarget: Long = -1,
        private var onFalseTarget: Long = -1,
    ) {
        fun id(lambda: () -> Long) { this.id = lambda() }
        fun items(lambda: () -> List<Item>) { this.items = lambda().toMutableList() }
        fun operation(lambda: () -> Long.() -> Long) { this.operation = lambda() }
        fun divisibleBy(lambda: () -> Long) { this.divisibleBy = lambda() }
        fun onTrueTarget(lambda: () -> Long) { this.onTrueTarget = lambda() }
        fun onFalseTarget(lambda: () -> Long) { this.onFalseTarget = lambda() }
        fun finalize() = Monkey(id, items, operation, divisibleBy, onTrueTarget, onFalseTarget)
    }

    private fun monkey(lambda: MonkeyBuilder.() -> Unit): MonkeyBuilder {
        return MonkeyBuilder().apply(lambda)
    }

    data class Operation(val transform: Long.() -> Long) {
        companion object {
            fun of(s: String): Operation {
                val regex = """(\w+)\s+([+*])\s+(\w+)""".toRegex()
                val (op1, op, op2) = regex.find(s.trim())!!.destructured

                return when (op) {
                    "+" -> when {
                        op1 != "old" -> Operation { this + op1.toInt() }
                        op2 != "old" -> Operation { this + op2.toInt() }
                        else -> Operation { this + this }
                    }
                    "*" -> when {
                        op1 != "old" -> Operation { this * op1.toInt() }
                        op2 != "old" -> Operation { this * op2.toInt() }
                        else -> Operation { this * this }
                    }
                    else -> error("Unsupported operator: $op")
                }
            }
        }
    }
}
