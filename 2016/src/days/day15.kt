package days

class Day15 : IDaySolver {
    override var parser: IParser = Day15Parser()
    override var problemInput: List<String>
    private val discs: MutableList<Disc>

    // Input is the list of discs, how many positions they have and their initial position
    constructor(input: List<String>) {
        this.problemInput = input
        this.discs = mutableListOf()
        for (line in this.problemInput) this.discs.add(Disc(line))
    }

    override fun test(): String {
        return findPerfectTime().toString()
    }

    override fun part1(): String {
        return findPerfectTime().toString()
    }
    // Difference from part 1 is there is another disc at the bottom, with the configuration specified
    override fun part2(): String {
        this.discs.add(Disc("Disc #7 has 11 positions; at time=0, it is at position 0."))
        return findPerfectTime().toString()
    }

    private fun findPerfectTime(): Int {
        var ticks = 0
        while (true) {
            // Check if all the disks are at their target position, if yes then we've reached the moment to drop
            if (discs.all { it.isAtTargetPosition() }) return ticks
            // Otherwise wait a second and update all the discs to their next position
            ticks++
            discs.forEach { disc -> disc.updatePosition() }
        }
    }
}

private class Day15Parser : IParser

private class Disc {
    private val discNumber: Int
    private var position: Int
    private val maxPositions: Int

    constructor(text: String) {
        val split = text.split(" ")
        // Format is #1
        this.discNumber = split[1].drop(1).toInt()
        this.maxPositions = split[3].toInt()
        // Remove period
        this.position = split.last().trim { it == '.' }.toInt()
    }

    // After a tick it moves to the next possible position, up one, coming back around if necessary
    fun updatePosition() {
        position = (position + 1) % maxPositions
    }

    // Check if it is at the target position. It is at the target position if its position after the specified
    // amount of ticks is 0. The ticks equal the disc number, it takes 1 tick to reach disc number 1, 2 for disc 2, etc.
    fun isAtTargetPosition(): Boolean {
        return ((position + discNumber) % maxPositions) == 0
    }
}