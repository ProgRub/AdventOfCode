package days

class Day16 : IDaySolver {
    override var parser: IParser = Day16Parser()
    override var problemInput: List<String>

    // Input is just a line with the initial state
    constructor(input: List<String>) {
        this.problemInput = input
    }

    override fun test(): String {
        var initialBoolean = BooleanArray(0)
        problemInput[0].forEach { initialBoolean += it == '1' }
        val finalInput = modifiedDragonCurveString(initialBoolean, 20)
        var checksum = ""
        findChecksum(finalInput).forEach { checksum += if (it) "1" else "0" }
        return checksum
    }

    override fun part1(): String {
        var initialBoolean = BooleanArray(0)
        problemInput[0].forEach { initialBoolean += it == '1' }
        val finalInput = modifiedDragonCurveString(initialBoolean, 272)
        var checksum = ""
        findChecksum(finalInput).forEach { checksum += if (it) "1" else "0" }
        return checksum
    }

    // Only thing that changes from part 1 to 2 is the target length of the string
    // Resolved with help
    override fun part2(): String {
        val finalInput = dragonCurve(problemInput[0].map { it == '1' }.toBooleanArray(), 35651584)
        var checksum = ""
        findChecksum(finalInput).forEach { checksum += if (it) "1" else "0" }
        return checksum
    }

    // Recursive function that builds the string until it reaches the target length by doing the following steps:
    // Copy the base string; reverse the order of the characters in the copy;
    // Reverse the bits in the copy: 0 to 1, 1 to 0. The new string is the base string + '0' + the copy
    private fun modifiedDragonCurveString(base: BooleanArray, targetLength: Int): BooleanArray {
        var result = base + false
        for (index in base.size - 1 downTo 0) {
            result += !base[index]
        }
        if (result.size >= targetLength) return result.sliceArray(0..<targetLength)
        return modifiedDragonCurveString(result, targetLength)
    }

    // Version of the function above that does the recursion the specified amount of times
    private fun modifiedDragonCurveString(base: String, times: Int, timesDone: Int): String {
        var result = "${base}0"
        for (index in base.length - 1 downTo 0) {
            result += when (base[index]) {
                '0' -> "1"
                '1' -> "0"
                else -> return ""
            }
        }
        if (timesDone + 1 == times) return result
        return modifiedDragonCurveString(result, times, timesDone + 1)
    }

    // Find the checksum of a string. We get the checksum of a string by comparing non-overlapping pairs
    // If the 2 characters match, checksum gets a '1', otherwise it gets a '0'
    // If the resulting checksum has an even length, we keep applying the process, now to the intermediate checksu
    // unitl we reach a checksum with an odd length
    private fun findChecksum(bools: BooleanArray): BooleanArray {
        val checksum = BooleanArray(bools.size / 2)
        var indexBools = 0
        var indexChecksum = 0
        while (indexBools < bools.size) {
            checksum[indexChecksum] = bools[indexBools] == bools[indexBools + 1]
            indexBools += 2
            indexChecksum++
        }
        if (checksum.size % 2 != 0) return checksum
        return findChecksum(checksum)
    }

    // Non-recursive method to optimize for part 2
    private fun dragonCurve(base: BooleanArray, targetLength: Int): BooleanArray {
        var result = base
        while (result.size < targetLength) {
            val reversedInverted = BooleanArray(result.size)
            for (i in result.indices) reversedInverted[result.size - 1 - i] = !result[i]
            val combined = BooleanArray(result.size * 2 + 1)
            result.copyInto(combined, 0)
            combined[result.size] = false // '0' separator
            reversedInverted.copyInto(combined, result.size + 1)
            result = combined
        }
        return result.copyOf(targetLength)
    }
}

private class Day16Parser : IParser