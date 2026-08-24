package days

class Day3 : IDaySolver {
    override var parser: IParser = Day3Parser()
    override var problemInput: List<String>

    // Input data consists of lines composed of 3 values, separated by spaces,
    // where each value is a side's length of a triangle
    constructor(input: List<String>) {
        this.problemInput = input
    }

    override fun test(): String {
        return ""
    }

    override fun part1(): String {
        return (problemInput.size - determineNumberInvalidTriangles()).toString()
    }

    override fun part2(): String {
        // Since instead of a line representing a triangle, it's each set of 3 values in a column,
        // We convert the input into a grid
        val valuesGrid = parser.parseTextToGrid(problemInput, "\\s+")
        val intGrid = valuesGrid.map { it.map { it.toInt() } }
        return (problemInput.size - determineNumberInvalidTriangles(intGrid)).toString()
    }

    // Determines how many of the sets of 3 are invalid triangles
    // The sets of 3 are made column wise, like relational table
    private fun determineNumberInvalidTriangles(grid: List<List<Int>>): Int {
        var invalidTriangles = 0
        var rowStart = 0
        while (rowStart < grid.size) {
            invalidTriangles += if (isInvalidTriangle(
                    listOf<Int>(
                        grid[rowStart][0],
                        grid[rowStart + 1][0],
                        grid[rowStart + 2][0]
                    )
                )
            ) 1 else 0
            invalidTriangles += if (isInvalidTriangle(
                    listOf<Int>(
                        grid[rowStart][1],
                        grid[rowStart + 1][1],
                        grid[rowStart + 2][1]
                    )
                )
            ) 1 else 0
            invalidTriangles += if (isInvalidTriangle(
                    listOf<Int>(
                        grid[rowStart][2],
                        grid[rowStart + 1][2],
                        grid[rowStart + 2][2]
                    )
                )
            ) 1 else 0
            rowStart += 3
        }
        return invalidTriangles
    }

    // Determines how many of the lines in the input are invalid triangles
    private fun determineNumberInvalidTriangles(): Int {
        var invalidTriangles = 0
        for (line in problemInput) {
            invalidTriangles += if (isInvalidTriangle(line.trim())) 1 else 0
        }
        return invalidTriangles
    }

    // Determines if the values in the list represent an invalid triangle
    // A triangle is only valid if the sum of any 2 sides is greater than the remaining side
    private fun isInvalidTriangle(valuesList: List<Int>): Boolean = when {
        valuesList[0] + valuesList[1] <= valuesList[2] -> true
        valuesList[0] + valuesList[2] <= valuesList[1] -> true
        valuesList[1] + valuesList[2] <= valuesList[0] -> true
        else -> false
    }

    // Determines if the values in the line represents an invalid triangle
    // A triangle is only valid if the sum of any 2 sides is greater than the remaining side
    private fun isInvalidTriangle(line: String): Boolean {
        val valuesList = line.split("\\s+".toRegex()).map { it.trim().toInt() }
        return when {
            valuesList[0] + valuesList[1] <= valuesList[2] -> true
            valuesList[0] + valuesList[2] <= valuesList[1] -> true
            valuesList[1] + valuesList[2] <= valuesList[0] -> true
            else -> false
        }
    }
}

private class Day3Parser : IParser {
    override fun parseTextToGrid(lines: List<String>, separator: String): List<List<String>> {
        if (separator == "\\s+") return lines.map { it.trim().split("\\s+".toRegex()) } // split by whitespaces
        return super.parseTextToGrid(lines, separator)
    }
}