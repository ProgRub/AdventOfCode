package days

class Day8 : IDaySolver {
    override var parser: IParser = Day8Parser()
    override var problemInput: List<String>
    private var day8Parser: Day8Parser = Day8Parser()
    private val width = 50
    private val height = 6
    private val screenGrid: MutableList<MutableList<Int>>
    private val instructionsList: List<Instruction>
    private val lastRectangleInstructionIndex: Int

    // The input is a list of instructions, each one is a line, to apply to the grid
    constructor(input: List<String>) {
        this.problemInput = input
        screenGrid = mutableListOf()
        repeat(height) {
            val row = mutableListOf<Int>()
            repeat(width) {
                row.add(0)
            }
            screenGrid += row
        }
        instructionsList = problemInput.map { day8Parser.parseInstruction(it) }
        lastRectangleInstructionIndex = instructionsList.indexOfLast { it.type == InstructionType.RECTANGLE }
    }

    override fun test(): String {
        findFinalDisplay()
        return screenGrid.sumOf { it.sum() }.toString()
    }

    override fun part1(): String {
        findFinalDisplay()
        return screenGrid.sumOf { it.sum() }.toString()
    }

    override fun part2(): String {
        findFinalDisplay()
        return printGrid()
    }

    // Print in a user-friendly manner the grid
    private fun printGrid(): String {
        var output = ""
        for (y in 0 until height) {
            for (x in 0 until width) {
                output += when (screenGrid[y][x]) {
                    1 -> "#"
                    0 -> " "
                    else -> ""//Doesn't happen
                }
            }
            output += "\n"
        }
        return output
    }

    // Goes through the list of instructions, applying them to find the final state of the display
    private fun findFinalDisplay() {
        for (instruction in instructionsList) {
            // THe condition below is an optimization only valid for part 1, for part 2 all instructions need to run
            // If we're past the last rectangle instruction then no more pixels are going to get turned on
            // if (index > this.lastRectangleInstructionIndex) return
            when (instruction.type) {
                InstructionType.ROTATE_ROW -> rotateRow(instruction.target, instruction.offset)
                InstructionType.ROTATE_COLUMN -> rotateColumn(instruction.target, instruction.offset)
                else -> turnOnRectangle(instruction.target, instruction.offset)
            }
        }
    }

    // "Rotates", or shifts to the right, the target row by offset, looping back around from the right to the left
    private fun rotateRow(target: Int, offset: Int) {
        val rowBefore: MutableList<Int> = mutableListOf()
        var index = 0
        // Store the column before the shift
        while (index < width) {
            rowBefore += screenGrid[target][index]
            index++
        }
        index = 0
        // We now fill out the column after the shift
        while (index < width) {
            screenGrid[target][(index + offset) % width] = rowBefore[index]
            index++
        }
    }

    // "Rotates", or shifts down, the target column by offset, looping back around from the right to the left
    private fun rotateColumn(target: Int, offset: Int) {
        val columnBefore: MutableList<Int> = mutableListOf()
        var index = 0
        // Store the column before the shift
        while (index < height) {
            columnBefore += screenGrid[index][target]
            index++
        }
        index = 0
        // We now fill out the column after the shift
        while (index < height) {
            screenGrid[(index + offset) % height][target] = columnBefore[index]
            index++
        }
    }

    // Turns on the pixels in the grid, starting from 0,0 (top left corner) until the width and height specified
    private fun turnOnRectangle(width: Int, height: Int) {
        repeat(height) { row -> repeat(width) { column -> this.screenGrid[row][column] = 1 } }
    }
}

private class Day8Parser : IParser {
    fun parseInstruction(text: String): Instruction =
        when {
            // Example: rect 3x2
            text.startsWith("rect") -> {
                val split = text.substring(5).split('x')
                Instruction(
                    InstructionType.RECTANGLE,
                    split[0].toInt(),
                    split[1].toInt()
                )
            }
            // Example: rotate row y=0 by 4
            text.startsWith("rotate row") ->
                Instruction(
                    InstructionType.ROTATE_ROW,
                    text.substring(13, 14).toInt(),
                    text.substring(18).toInt()
                )
            // Example: rotate column x=1 by 1
            text.startsWith("rotate column") -> {
                val split = text.split(' ')
                Instruction(
                    InstructionType.ROTATE_COLUMN,
                    split[2].substring(2).toInt(),
                    split.last().toInt()
                )
            }

            else -> Instruction(InstructionType.RECTANGLE, 0, 0)
        }
}

private data class Instruction(val type: InstructionType, val target: Int, val offset: Int)

private enum class InstructionType { RECTANGLE, ROTATE_ROW, ROTATE_COLUMN }