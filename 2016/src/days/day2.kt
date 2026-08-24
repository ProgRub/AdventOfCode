package days

import kotlin.math.pow

class Day2 : IDaySolver {
    override var parser: IParser = Day2Parser()
    override var problemInput: List<String>
    val numpadGridSize = 3

    // Input data consists of a list of lines with the moves the finger makes to input the code
    constructor(input: List<String>) {
        this.problemInput = input
    }

    override fun test(): String {
        return findBathroomCodeNumpadGrid().toString()
    }

    override fun part1(): String {
        return findBathroomCodeNumpadGrid().toString()
    }

    // The grid for part 2 is this monstrosity
    //     1
    //   2 3 4
    // 5 6 7 8 9
    //   A B C
    //     D
    override fun part2(): String {
        val grid = listOf<String>(
            "  1  ",
            " 234 ",
            "56789",
            " ABC ",
            "  D  "
        )
        return findBathroomCodeAlphaGrid(grid)
    }

    // Finds the bathroom code, where the grid is alphanumeric and can have any shape, it's passed as parameter
    private fun findBathroomCodeAlphaGrid(grid: List<String>): String {
        var code = ""
        var startPoint = '5'
        var endPoint: Char
        for (lineOfMoves in problemInput) {
            // find the spot starting from the previous spot and making the moves specified
            endPoint = findNextChar(startPoint, lineOfMoves, grid)
            code += endPoint
            startPoint = endPoint // To find the next spot we start from the spot found this cycle
        }
        return code
    }

    // Finds the next number by making the moves specified starting from startPoint
    private fun findNextChar(startPoint: Char, moves: String, grid: List<String>): Char {
        var endPoint = startPoint
        val gridSize = grid.size
        // Index goes from 0 (top left corner of the grid) to gridSize*gridSize (bottom right corner of the grid)
        var index = 0
        var lastIndex = -1
        // Find the "index" of the start point
        for (line in grid) {
            for (space in line) {
                if (space == startPoint) {
                    lastIndex = index
                    break
                }
                index++
            }
            if (lastIndex != -1) break
        }
        for (move in moves) {
            // If the move can't be done we stay on the same spot
            index = when (move) {
                'U' -> if (index < gridSize) index else index - gridSize // Up on the numpad
                'R' -> if ((index + 1) % gridSize == 0) index else index + 1 // Right on the numpad
                'L' -> if (index % gridSize == 0) index else index - 1 // Left on the numpad
                'D' -> if ((index + gridSize) > gridSize * gridSize) index else index + gridSize // Down on the numpad
                else -> -1 // Can't happen
            }
            // If the next move is to a blank button we can't make it, reset index
            if (grid[index / gridSize].elementAt(index % gridSize) == ' ') index = lastIndex
            lastIndex = index
            // Dividing by gridSize gets us the row, the remainder is the column
            endPoint = grid[index / gridSize].elementAt(index % gridSize)
        }
        return endPoint
    }

    // Finds the bathroom code, where the grid is a normal 3x3 numpad grid
    private fun findBathroomCodeNumpadGrid(): Int {
        var code = 0
        // Size of the input is equal to the number of numbers we need to find
        // It's 10 to the size in order to build the code from left to right
        var multiplier = 10.0.pow(problemInput.size - 1).toInt()
        var startPoint = 5
        var endPoint: Int
        for (lineOfMoves in problemInput) {
            // find the number starting from the previous number and making the moves specified
            endPoint = findNumber(startPoint, lineOfMoves)
            // add the number where we ended up, building the code from left to right
            code += endPoint * multiplier
            multiplier /= 10 // Divide by 10 to find the next digit in the sequence
            startPoint = endPoint // To find the next number we start from the number found this cycle
        }
        return code
    }

    // Finds the next number by making the moves specified starting from startPoint
    private fun findNumber(startPoint: Int, moves: String): Int {
        var number = startPoint
        for (move in moves) {
            // If the move can't be done we stay on the same number
            // Example, trying to go up from 2 stays on 2
            number = when (move) {
                'U' -> if (number <= numpadGridSize) number else number - numpadGridSize // Up on the numpad
                'R' -> if (number % numpadGridSize == 0) number else number + 1 // Right on the numpad
                'L' -> if (number % numpadGridSize == 1) number else number - 1 // Left on the numpad
                'D' -> if (number + numpadGridSize > numpadGridSize * numpadGridSize) number else number + numpadGridSize // Down on the numpad
                else -> -1 // Can't happen
            }
        }
        return number
    }
}

private class Day2Parser : IParser