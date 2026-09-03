package days

import kotlin.math.max

class Day20 : IDaySolver {
    override var parser: IParser = Day20Parser()
    override var problemInput: List<String>
    private val ipRanges: MutableList<Range>

    // Input is a list of IP ranges blacklisted, in the format 10-15
    constructor(input: List<String>) {
        this.problemInput = input
        ipRanges = mutableListOf()
        for (ipRange in problemInput.sorted()) {
            val split = ipRange.split("-")
            ipRanges += Range(split[0].toLong(), split[1].toLong())
        }
    }

    override fun test(): String {
        return findLowestPossibleIP(ipRanges).toString()
    }

    override fun part1(): String {
        return findLowestPossibleIP(ipRanges).toString()
    }

    override fun part2(): String {
        return findAmountAvailableIps(ipRanges).toString()
    }

    // Find the lowest IP that isn't blacklisted
    private fun findLowestPossibleIP(ipRanges: List<Range>): Long {
        val rangesSorted = ipRanges.sortedBy { it.low } // Sort the ranges first to make it easier
        var highestIpBlacklisted = rangesSorted.first().high
        for (range in rangesSorted.subList(1, ipRanges.size)) {
            // If there's a hole between the ranges then we have the lowest possible IP
            if (range.low - 1 > highestIpBlacklisted) return highestIpBlacklisted + 1
            // Otherwise keep checking, ranges can overlap so we only care about the highest IP blacklisted
            highestIpBlacklisted = max(highestIpBlacklisted, range.high)
        }
        return 0
    }

    // Find the amount of IP's that aren't blacklisted; max'ed with 0 because the difference can be negative
    private fun findAmountAvailableIps(ipRanges: List<Range>): Long {
        val rangesSorted = ipRanges.sortedBy { it.low } // Sort the ranges first to make it easier
        val lowestPossibleIP = 0
        val highestPossibleIP = 4294967295
        // Initially the amount available is the IP's from the lowest until the first IP blacklisted
        var amountAvailable = max(rangesSorted.first().low - 1 - lowestPossibleIP, 0)
        var highestIpBlacklisted = rangesSorted.first().high // Holds the highest IP that's blacklisted
        for (range in rangesSorted.subList(1, ipRanges.size - 1)) {
            // We add the number of IP's available below the current range and greater than the last IP blacklisted
            amountAvailable += max(range.low - 1 - highestIpBlacklisted, 0)
            highestIpBlacklisted = max(highestIpBlacklisted, range.high)
        }
        // THe rest of the IP's from the highest IP blacklisted, exclusively, until the highest IP possible
        amountAvailable += max(highestPossibleIP - highestIpBlacklisted, 0)
        return amountAvailable
    }
}

private class Day20Parser : IParser

private class Range(val low: Long, val high: Long)