package days

import kotlin.math.max
import kotlin.math.min

class Day21 : IDaySolver {
    override var parser: IParser = Day21Parser()
    override var problemInput: List<String>
    private val initialPassword: String
    private val instructions: List<String>

    // Input starts with the initial password (first line)
    // All other lines are the instructions to apply in sequence to the initial password
    constructor(input: List<String>) {
        this.problemInput = input
        initialPassword = this.problemInput[0]
        instructions = this.problemInput.subList(1, this.problemInput.size)
    }

    override fun test(): String {
        return findScrambledPassword(initialPassword, instructions)
    }

    override fun part1(): String {
        return findScrambledPassword(initialPassword, instructions)
    }

    override fun part2(): String {
        return unscramblePassword("fbgdceah", instructions)
    }

    // Performs the list of scrambling instructions on the initial password, returning the end result
    private fun findScrambledPassword(password: String, scramblingInstructions: List<String>): String {
        var scrambledPassword = password
        for (instruction in scramblingInstructions) {
            val split = instruction.split(" ")
            when {
                // Swap letters at positions specified
                instruction.startsWith("swap position") -> {
                    val firstPosition = split[2].toInt()
                    val secondPosition = split[5].toInt()
                    scrambledPassword = swapPositions(scrambledPassword, firstPosition, secondPosition)
                }
                // Swap letters in the string
                instruction.startsWith("swap letter") -> {
                    val firstLetter = split[2]
                    val secondLetter = split[5]
                    scrambledPassword = swapLetters(scrambledPassword, firstLetter, secondLetter)
                }
                // Rotate the string right the amount of times specified by the position of the letter plus one
                // And plus one again if the index of the letter is greater or equal to 4
                instruction.startsWith("rotate based") -> {
                    val indexOfLetter = scrambledPassword.indexOf(split.last())
                    var rotationAmount = indexOfLetter + 1
                    if (indexOfLetter >= 4) rotationAmount++
                    scrambledPassword = rotateString(scrambledPassword, rotationAmount)
                }
                // Rotate the string left or right the amount of steps specified
                instruction.startsWith("rotate") -> {
                    var rotationAmount = split[2].toInt()
                    if (split[1] == "left") rotationAmount *= -1 // If we're rotating left, amount should be negative
                    scrambledPassword = rotateString(scrambledPassword, rotationAmount)
                }
                // Reverse the substring specified by the positions, including both
                instruction.startsWith("reverse") -> {
                    val initialPosition = split[2].toInt()
                    val lastPosition = split[4].toInt()
                    scrambledPassword = reverseSubstring(scrambledPassword, initialPosition, lastPosition)
                }
                // Move letter at position X to position Y
                instruction.startsWith("move") -> {
                    val positionToMove = split[2].toInt()
                    val endPosition = split[5].toInt()
                    scrambledPassword = moveToPosition(scrambledPassword, positionToMove, endPosition)
                }
            }
        }
        return scrambledPassword
    }

    // Reverse engineers the scrambled password to find the initial password, returning the end result
    private fun unscramblePassword(scrambledPassword: String, scramblingInstructions: List<String>): String {
        var initialPassword = scrambledPassword
        for (instruction in scramblingInstructions.reversed()) {
            val split = instruction.split(" ")
            // Since we're reverse engineering, the arguments are flipped to go from the scrambled to the unscrambled
            when {
                // Swap letters at positions specified
                instruction.startsWith("swap position") -> {
                    val firstPosition = split[5].toInt()
                    val secondPosition = split[2].toInt()
                    initialPassword = swapPositions(initialPassword, firstPosition, secondPosition)
                }
                // Swap letters in the string
                instruction.startsWith("swap letter") -> {
                    val firstLetter = split[5]
                    val secondLetter = split[2]
                    initialPassword = swapLetters(initialPassword, firstLetter, secondLetter)
                }
                // Rotate the string right the amount of times specified by the position of the letter plus one
                // And plus one again if the index of the letter is greater or equal to 4
                instruction.startsWith("rotate based") -> {
                    val indexAfterRotation = initialPassword.indexOf(split.last())
                    // pos rotation new pos
                    //   7        9       0
                    //   0        1       1
                    //   4        6       2
                    //   1        2       3
                    //   5        7       4
                    //   2        3       5
                    //   6        8       6
                    //   3        4       7
                    // Based on the table, find the rotation amount back to the original position
                    val rotationAmount = listOf(-1, -1, 2, -2, 1, -3, 0, -4)[indexAfterRotation]
                    initialPassword = rotateString(initialPassword, rotationAmount)
                }
                // Rotate the string left or right the amount of steps specified
                instruction.startsWith("rotate") -> {
                    var rotationAmount = split[2].toInt()
                    // Reverse engineering, if the password was rotated right to go back we rotate left, hence negative
                    if (split[1] == "right") rotationAmount *= -1
                    initialPassword = rotateString(initialPassword, rotationAmount)
                }
                // Reverse the substring specified by the positions, including both
                // This instruction stays the same, it's the same substring that needs to be reversed
                instruction.startsWith("reverse") -> {
                    val initialPosition = split[2].toInt()
                    val lastPosition = split[4].toInt()
                    initialPassword = reverseSubstring(initialPassword, initialPosition, lastPosition)
                }
                // Move letter at position X to position Y
                instruction.startsWith("move") -> {
                    val positionToMove = split[5].toInt()
                    val endPosition = split[2].toInt()
                    initialPassword = moveToPosition(initialPassword, positionToMove, endPosition)
                }
            }
        }
        return initialPassword
    }

    // Moves the letter at position to move to the end position specified, returning the resulting string
    private fun moveToPosition(password: String, positionToMove: Int, endPosition: Int): String {
        val letterMoved = password.elementAt(positionToMove)
        val passwordWithoutLetterMoved = password.replace(letterMoved.toString(), "")
        return passwordWithoutLetterMoved.substring(0, endPosition) +
                letterMoved +
                passwordWithoutLetterMoved.substring(endPosition)
    }

    // Reverses the substring specified by the positions, inclusive of both; returns the resulting string
    // with the reversed substring
    private fun reverseSubstring(password: String, initialPosition: Int, lastPosition: Int): String {
        val substring = password.substring(initialPosition, lastPosition + 1)
        return password.substring(0, initialPosition) +
                substring.reversed() +
                password.substring(lastPosition + 1)
    }

    // Rotates the string to the right, if rotation amount >= 0, or to the left, by the amount specified
    // Returns the resulting string
    private fun rotateString(password: String, rotationAmount: Int): String {
        var newPassword = ""
        val realRotationAmount = rotationAmount % password.length
        // If we're rotating left, the first character is the one specified by the rotation amount
        // If we're going right, we need to start at the far end of the string
        var index = if (realRotationAmount < 0) realRotationAmount * -1 else password.length - realRotationAmount
        while (newPassword.length < password.length) {
            newPassword += password[index % password.length]
            index++
        }
        return newPassword
    }

    // Swaps the letters specified, using the swapPositions function, returning the resulting string
    private fun swapLetters(password: String, firstLetter: String, secondLetter: String): String {
        val indexOfFirstLetter = password.indexOf(firstLetter)
        val indexOfSecondLetter = password.indexOf(secondLetter)
        return swapPositions(password, indexOfFirstLetter, indexOfSecondLetter)
    }

    // Swaps the letters in the positions specified, returning the resulting string
    private fun swapPositions(password: String, positionA: Int, positionB: Int): String {
        val firstPosition = min(positionA, positionB)
        val secondPosition = max(positionA, positionB)
        return password.substring(0, firstPosition) +
                password.elementAt(secondPosition) +
                password.substring(firstPosition + 1, secondPosition) +
                password.elementAt(firstPosition) + password.substring(secondPosition + 1)
    }
}

private class Day21Parser : IParser