object Day08 : Runner<Int, Int>(8, 21, 8) {
    override fun part1(input: List<String>): Int {
        val height = input.size
        val treePatch = TreePatch(List(height) { input[it].toCharArray().map { c -> Tree(c.digitToInt()) } })
        val width = treePatch.trees[0].size

        for (x in 0 until width) {
            for (y in 0 until height) {
                val otherTrees = treePatch.cross(x, y)
                val tree = treePatch.trees[x][y]
                tree.isVisible = tree.isVisible(otherTrees)
            }
        }

        return treePatch.trees.flatten().count { tree -> tree.isVisible ?: false }
    }

    override fun part2(input: List<String>): Int {
        val height = input.size
        val treePatch = TreePatch(List(height) { input[it].toCharArray().map { c -> Tree(c.digitToInt()) } })
        val width = treePatch.trees[0].size

        for (x in 0 until width) {
            for (y in 0 until height) {
                val otherTrees = treePatch.cross(x, y)
                val tree = treePatch.trees[x][y]
                tree.scenicScore = tree.countVisibleTrees(otherTrees.left) *
                    tree.countVisibleTrees(otherTrees.right) *
                    tree.countVisibleTrees(otherTrees.top) *
                    tree.countVisibleTrees(otherTrees.bottom)
            }
        }

        return treePatch.trees.flatten().maxOf { it.scenicScore }
    }

    data class TreePatch(
        val trees: List<List<Tree>>
    ) {
        fun cross(x: Int, y: Int) = Cross(
            left = trees[x].subList(0, y).asReversed(),
            right = trees[x].subList(y + 1, width),
            top = (0 until x).map { trees[it][y] }.asReversed(),
            bottom = (x + 1 until height).map { trees[it][y] }
        )

        private val width by lazy { trees.first().size }
        private val height by lazy { trees.size }
    }

    data class Cross(
        val left: List<Tree>,
        val right: List<Tree>,
        val top: List<Tree>,
        val bottom: List<Tree>
    )

    data class Tree(
        val height: Int,
        var isVisible: Boolean? = null,
        var scenicScore: Int = -1
    ) {
        override fun toString(): String {
            return height.toString()
        }

        fun isVisible(cross: Cross): Boolean {
            return cross.left.all { it.height < height } ||
                cross.right.all { it.height < height } ||
                cross.top.all { it.height < height } ||
                cross.bottom.all { it.height < height }
        }

        fun countVisibleTrees(list: List<Tree>): Int {
            return when {
                list.isEmpty() -> 0
                list.none { it.height >= height } -> list.size
                else -> list.indexOfFirst { it.height >= height } + 1
            }
        }
    }
}
