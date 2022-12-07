object Day07 : Runner<Int>(7, 95437, 24933642) {
    override fun part1(input: List<String>): Int {
        val fs = Directory(null, "")

        buildFileSystem(fs, input)

        return allDirectories(fs).filter { it.size() <= 100000 }.sumOf { it.size() }
    }

    private fun buildFileSystem(fs: Directory, input: List<String>) {
        var pwd = fs
        var prevCommand: Command<*>? = null

        input.forEach {
            when {
                it.startsWith("$") -> when (it.split(" ")[1]) {
                    "cd" -> {
                        pwd = Cd(it.split(" ")[2]).also { c -> prevCommand = c }.exec(pwd)
                    }
                    "ls" -> prevCommand = Ls
                }
                else -> {
                    when (prevCommand) {
                        Ls -> {
                            val tokens = it.split(" ")
                            when (tokens[0]) {
                                "dir" -> {
                                    pwd.add(Directory(pwd, tokens[1]))
                                }
                                else -> {
                                    pwd.add(File(tokens[0].toInt(), tokens[1]))
                                }
                            }
                        }
                        else -> error("don't know what this output is")
                    }
                }
            }
        }
    }

    private fun allDirectories(fs: Directory): Set<Directory> {
        val directories = mutableSetOf(fs)

        var children = directories.flatMap { it.children }.filterIsInstance<Directory>()

        while (children.isNotEmpty()) {
            directories.addAll(children)
            children = directories.flatMap { it.children }.filterIsInstance<Directory>() - directories.toSet()
        }

        return directories
    }

    override fun part2(input: List<String>): Int {
        val totalDiskSpace = 70000000
        val requiredSpace = 30000000

        val fs = Directory(null, "")

        buildFileSystem(fs, input)

        val spaceNeededToFree = requiredSpace - (totalDiskSpace - fs.size())

        return allDirectories(fs).map { it.size() }.filter { it >= spaceNeededToFree }.min()
    }

    sealed interface Input
    sealed interface Command<T> : Input {
        fun exec(fs: Directory): T
    }
    data class Cd(val path: String) : Command<Directory> {
        override fun exec(fs: Directory): Directory {
            var pwd = fs
            when (path) {
                "/" -> while (pwd.parent != null) pwd = pwd.parent!!
                ".." -> pwd = pwd.parent ?: pwd
                else -> {
                    val dir = Directory(pwd, path)
                    pwd.add(dir)
                    pwd = dir
                }
            }
            return pwd
        }
    }

    object Ls : Command<Set<Item>> {
        override fun exec(fs: Directory): Set<Item> = fs.children

        override fun toString() = "ls"
    }

    sealed interface Item : Input {
        fun size(): Int
    }
    data class File(private val fileSize: Int, val name: String) : Item {
        override fun size() = fileSize
    }

    class Directory(
        var parent: Directory? = null,
        private val name: String,
        val children: MutableSet<Item> = mutableSetOf()
    ) : Item {
        override fun size() = children.sumOf { it.size() }
        fun add(item: Item) = children.add(item)

        override fun toString(): String {
            return if (parent == null) {
                name.ifBlank { "/" }
            } else {
                "$parent$name/"
            }
        }
    }
}
