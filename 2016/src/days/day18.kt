package days

class Day18 : IDaySolver {
    override var parser: IParser = Day18Parser()
    override var problemInput: List<String>
    private val safeChar = '.'
    private val trapChar = '^'

    // Input is just the first line of tiles
    constructor(input: List<String>) {
        this.problemInput = input
    }

    override fun test(): String {
        val grid = buildTileGrid(10, problemInput[0])
        for (row in grid) println(row)
        return grid.sumOf { it.count { it == safeChar } }.toString()
    }

    override fun part1(): String {
        val grid = buildTileGrid(40, problemInput[0])
        return grid.sumOf { it.count { it == safeChar } }.toString()
    }

    // Just more rows for part 2
    override fun part2(): String {
        val grid = buildTileGrid(400000, problemInput[0])
        return grid.sumOf { it.count { it == safeChar } }.toString()
    }

    // Build grid of tiles, making the amount of rows specified based on the initial row
    private fun buildTileGrid(numberRows: Int, initialRow: String): List<String> {
        val grid = mutableListOf(initialRow)
        // We already have the first row
        repeat(numberRows - 1) {
            grid += makeNextTileRow(grid[it])
        }
        return grid
    }

    // Builds the next row of tiles based on the row of tiles passed)
    private fun makeNextTileRow(currentTileRow: String): String {
        var nextTileRow = ""
        for (index in currentTileRow.indices)
            nextTileRow += if (isTrap(index, currentTileRow)) trapChar else safeChar
        return nextTileRow
    }

    // Determines if the tile in the next row, at the given position, is a trap or not
    private fun isTrap(tileIndex: Int, tileRow: String): Boolean {
        // If the tile index is the leftmost or rightmost, we consider the right (or left, respectively) is safe
        val leftIsSafe = tileIndex - 1 < 0 || tileRow[tileIndex - 1] == safeChar
        val rightIsSafe = tileIndex + 1 == tileRow.length || tileRow[tileIndex + 1] == safeChar
        // The new tile is a trap if:
        // Its left and center tiles are traps, but its right tile is not.
        // Its center and right tiles are traps, but its left tile is not.
        // Only its left tile is a trap.
        // Only its right tile is a trap.
        // So we can see the center tile doesn't matter, what matter is the left and right tiles can't be traps
        // and cannot be safe at the same time, so it's an exclusive or
        return !leftIsSafe xor !rightIsSafe
    }
}

private class Day18Parser : IParser