package days

interface IDaySolver {
    var parser: IParser
    var problemInput: List<String>

    // Function to test against test input
    fun test(): String

    // Function to test against part 1 input
    fun part1(): String

    // Function to test against part 2 input
    fun part2(): String
}