object Day11 : Runner<Long, Long>(11, 10605L, 2713310158L) {

    private fun String.toItems(): List<Item> = split(",").map { Item(it.trim().toLong()) }

    object Regex {
        val monkey = """Monkey (\d+):""".toRegex()
        val itemList = """\s+Starting items: (.*)""".toRegex()
        val operation = """\s+Operation: new = (.*)""".toRegex()
        val divisibleTest = """\s+Test: divisible by (\d+)""".toRegex()
        val ifTrue = """\s+If true: throw to monkey (\d+)""".toRegex()
        val ifFalse = """\s+If false: throw to monkey (\d+)""".toRegex()
    }

    private fun monkeyMap(input: List<String>, factor: Long = 3): MutableMap<Int, Monkey> {
        val monkeyMap = mutableMapOf<Int, Monkey>()
        var currentMonkey = monkey { worryDecreaseFactor { factor } }

        input.forEach {
            if (it.trim().isBlank()) {
                val monkey = currentMonkey.finalize()
                monkeyMap[monkey.id] = monkey
                currentMonkey = monkey { worryDecreaseFactor { factor } }
            }

            Regex.monkey.find(it)?.destructured?.also {
                currentMonkey.apply { id { it.component1().toInt() } }
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
                currentMonkey.apply { onTrueTarget { it.component1().toInt() } }
            }

            Regex.ifFalse.find(it)?.destructured?.also {
                currentMonkey.apply { onFalseTarget { it.component1().toInt() } }
            }
        }

        val monkey = currentMonkey.finalize()
        monkeyMap[monkey.id] = monkey

        println(monkeyMap.values.joinToString("\n") { "${it.id} ${it.items}" })

        return monkeyMap
    }

    private fun MutableMap<Int, Monkey>.inspect(rounds: Int = 20) {
        val supermodulo = this.values.fold(1L) { a, m ->
            a * m.divisibleBy
        }

        repeat(rounds) { r ->
            this.entries.sortedBy { it.key }.forEach { (_, monkey) ->
                while (monkey.items.isNotEmpty()) {
                    val item = monkey.items.removeFirst()
                    monkey.throwItem(item, this[monkey.inspect(item, supermodulo)]!!)
                }
            }

            val round = r + 1
            if (rounds > 20 && (round == 1 || round == 20 || round % 1000 == 0)) {
                println("\n== After round $round ==")
                this.values.forEach { monkey ->
                    println("Monkey ${monkey.id} inspected items ${monkey.numberOfInspections} times.")
                }
            }
        }

        this.values.forEach {
            println("Monkey ${it.id} inspected items ${it.numberOfInspections} times.")
        }
    }

    private fun Map<Int, Monkey>.monkeyBusiness(): Long {
        return this.values
            .sortedByDescending { it.numberOfInspections }
            .take(2)
            .fold(1) { a, m -> a * m.numberOfInspections }
    }

    override fun part1(input: List<String>): Long {
        val monkeyMap = monkeyMap(input)

        monkeyMap.inspect()

        return monkeyMap.monkeyBusiness()
    }

    override fun part2(input: List<String>): Long {
        val monkeyMap = monkeyMap(input, 1)

        monkeyMap.inspect(10000)

        return monkeyMap.monkeyBusiness()
    }

    data class Item(var worryLevel: Long) {
        override fun toString(): String {
            return worryLevel.toString()
        }
    }
    data class Monkey(
        val id: Int,
        val items: MutableList<Item>,
        val operation: Long.() -> Long,
        val worryDecreaseFactor: Long,
        val divisibleBy: Long,
        val onTrueTarget: Int,
        val onFalseTarget: Int,
        var numberOfInspections: Long = 0L,
    ) {
        fun inspect(item: Item, modulo: Long): Int {
            numberOfInspections++

            item.worryLevel = item.worryLevel.operation() % modulo
            item.worryLevel /= worryDecreaseFactor

            return if (item.worryLevel % divisibleBy == 0L) onTrueTarget
            else onFalseTarget
        }

        fun throwItem(item: Item, monkey: Monkey) {
            monkey.items.add(item)
        }
    }

    class MonkeyBuilder(
        private var id: Int = -1,
        private var items: MutableList<Item> = mutableListOf(),
        private var operation: Long.() -> Long = { this },
        private var worryDecreaseFactor: Long = 3,
        private var divisibleBy: Long = 0,
        private var onTrueTarget: Int = -1,
        private var onFalseTarget: Int = -1,
    ) {
        fun id(lambda: () -> Int) { this.id = lambda() }
        fun worryDecreaseFactor(lambda: () -> Long) { this.worryDecreaseFactor = lambda() }
        fun items(lambda: () -> List<Item>) { this.items = lambda().toMutableList() }
        fun operation(lambda: () -> Long.() -> Long) { this.operation = lambda() }
        fun divisibleBy(lambda: () -> Long) { this.divisibleBy = lambda() }
        fun onTrueTarget(lambda: () -> Int) { this.onTrueTarget = lambda() }
        fun onFalseTarget(lambda: () -> Int) { this.onFalseTarget = lambda() }
        fun finalize() = Monkey(
            id = id,
            items = items,
            operation = operation,
            worryDecreaseFactor = worryDecreaseFactor,
            divisibleBy = divisibleBy,
            onTrueTarget = onTrueTarget,
            onFalseTarget = onFalseTarget
        )
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
