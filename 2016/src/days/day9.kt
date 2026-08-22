package days

import java.util.*

class Day9 : IDaySolver {
    override var parser: IParser = Day9Parser()
    override var problemInput: List<String>
    private val wordStack: Stack<Long>

    // Input is just one line, a very long string
    constructor(input: List<String>) {
        this.problemInput = input
        this.wordStack = Stack()
    }

    override fun test(): String {
        return decompressStringSimple()
    }

    override fun part1(): String {
        return decompressStringSimple().length.toString()
    }

    override fun part2(): String {
        return calculateLengthFullyDecompressedString(this.problemInput[0]).toString()
    }

    private fun calculateLengthFullyDecompressedString(
        currentString: String,
    ): Long {
        // Find next marker, if there isn't return the lenght of the string
        val indexStartMarker = currentString.indexOf('(')
        if (indexStartMarker == -1) return currentString.length.toLong()
        val indexEndMarker = currentString.indexOf(')')
        val marker = currentString.substring(indexStartMarker, indexEndMarker + 1)
        val markerDecomposed = decomposeMarker(marker)
        val numberCharacters = markerDecomposed[0] // Number of characters to repeat
        val repeatAmount = markerDecomposed[1] // The amount of times said characters are going to be repeated
        // The portion of the string prior to the marker
        val leftString = currentString.substring(0, indexStartMarker)
        // The portion of the string affected by the marker
        val middleString = currentString.substring(indexEndMarker + 1, indexEndMarker + 1 + numberCharacters)
        // The rest of the string after the sequence affected by the marker
        val rightString = currentString.substring(indexEndMarker + 1 + numberCharacters)
        return leftString.length +
                calculateLengthFullyDecompressedString(middleString) * repeatAmount +
                calculateLengthFullyDecompressedString(rightString)
    }

    // This function "decompresses" the string of the problem input but in the simple way, where if a marker
    // is copied by the decompression, that marker doesn't decompress the rest of the string recursively,
    // the copied marker stays in the decompressed string
    private fun decompressStringSimple(): String {
        var decompressed = ""
        val compressedString = this.problemInput[0]
        var index = 0
        while (index < compressedString.length) {
            // If we find the start of a marker, we decompress the string according to the marker instructions
            if (compressedString[index] == '(') {
                val endIndexMarker = compressedString.indexOf(')', index + 1)
                val marker = compressedString.substring(index, endIndexMarker + 1)
                // Add the decompressed excerpt from the marker to the decompressed string
                decompressed += decompressExcerpt(
                    compressedString,
                    marker,
                    index
                )
                // We have to skip the marker and the amount of characters that marker says to decompress
                index += endIndexMarker - index + findSkipAmount(marker)
            } else decompressed += compressedString[index] //otherwise we just add the character
            index++
        }
        return decompressed
    }

    // Finds the amount of characters that need to be skipped from the compressed string
    // The characters that were decompressed can't be added again to the decompressed string, so we skip
    // the amount of characters that were decompressed by the marker
    private fun findSkipAmount(marker: String): Int {
        // If it's an invalid marker exit
        if (!marker.startsWith('(') || !marker.endsWith(')')) return 0
        val splitMarker = marker.split('x')
        return splitMarker[0].substring(1).toInt()
    }

    // This function "decompresses" the portion of the compressed string specified by the marker
    // It has the format (10x1) where this means the next 10 characters after the marker are repeated once
    private fun decompressExcerpt(compressedString: String, marker: String, markerStartIndex: Int): String {
        // If it's an invalid marker exit
        if (!marker.startsWith('(') || !marker.endsWith(')')) return ""
        val markerDecomposed = decomposeMarker(marker)
        // The string that is going to be repeated is right after the marker
        val startIndexDecompress = markerStartIndex + marker.length
        // We need to repeat the text in front of the marker, how many characters it is, the amount of times specified
        return compressedString.substring(startIndexDecompress, startIndexDecompress + markerDecomposed[0])
            .repeat(markerDecomposed[1])
    }

    // Decompose the marker into the number of characters it affects and how many times they repeat
    private fun decomposeMarker(marker: String): List<Int> {
        // If it's an invalid marker exit
        if (!marker.startsWith('(') || !marker.endsWith(')')) return listOf(0, 0)
        val splitMarker = marker.split('x')
        // Number of characters to repeat
        val numberCharacters = splitMarker[0].substring(1).toInt()
        // The amount of times said characters are going to be repeated
        val repeatAmount = splitMarker[1].slice(0..<splitMarker[1].length - 1).toInt()
        return listOf(numberCharacters, repeatAmount)
    }
}

class Day9Parser : IParser