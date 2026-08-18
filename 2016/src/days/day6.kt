package days

class Day6 : IDaySolver {
    override var parser: IParser = Day6Parser()
    override var problemInput: List<String>

    // The input is a bunch of lines, seemingly jumbled up
    constructor(input: List<String>) {
        this.problemInput = input
    }

    override fun test(): String {
        return findMessage(true)
    }

    override fun part1(): String {
        return findMessage(true)
    }

    override fun part2(): String {
        return findMessage(false)
    }

    // Finds the message hidden in the input
    // The message consists of the most common character that appears in each column
    private fun findMessage(takeMostCommon: Boolean): String {
        val columns = this.problemInput[0].length // number of columns, all strings have the same length
        // The key of this map is the index, aka the characters' column, and the character
        // The value the amount of times the characters appears in that column
        val letterCountMap = mutableMapOf<String, Int>()
        for (line in this.problemInput) {
            for ((index, character) in line.withIndex()) {
                val key = index.toString() + character
                if (key in letterCountMap) letterCountMap[key] = letterCountMap[key]!! + 1
                else letterCountMap[key] = 1
            }
        }
        var message = ""
        for (index in 0..<columns) {
            // Filter the counted letters map to the index in question
            val indexMap = letterCountMap.filter { it.key.startsWith(index.toString()) }
            // When take most common is true, we need to find the most common letter in the column
            // Otherwise we want the least common
            val bestValue = when (takeMostCommon) {
                true -> indexMap.values.maxOf { it }
                else -> indexMap.values.minOf { it }
            }
            // Add to the message the most common/least common character (second character in key with highest count)
            message += indexMap.filter { it.value == bestValue }.keys.first()[1]
        }
        return message
    }
}

class Day6Parser : IParser