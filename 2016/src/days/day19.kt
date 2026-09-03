package days

class Day19 : IDaySolver {
    override var parser: IParser = Day19Parser()
    override var problemInput: List<String>
    private val elves: LinkedElf
    private val leftSideCircle: ArrayDeque<Int>
    private val rightSideCircle: ArrayDeque<Int>

    // Input is just the number of elves in the circle
    constructor(input: List<String>) {
        this.problemInput = input
        var count = 1
        leftSideCircle = ArrayDeque<Int>()
        rightSideCircle = ArrayDeque<Int>()
        this.elves = LinkedElf(1, null)
        val numberElves = this.problemInput[0].toInt()
        var pointerLast = this.elves
        while (count < numberElves) {
            if (count <= numberElves / 2) leftSideCircle.add(count)
            else rightSideCircle.addFirst(count)
            count++
            pointerLast.next = LinkedElf(count, null)
            pointerLast = pointerLast.next!!
        }
        rightSideCircle.addFirst(count) // Last one
        // Loop back around
        pointerLast.next = this.elves
    }

    override fun test(): String {
        return findLastElfRemoveLeft().toString()
    }

    override fun part1(): String {
        return findLastElfRemoveLeft().toString()
    }

    // Instead of elves taking from the ones to their left, they take from across the circle
    override fun part2(): String {
        return findLastElfRemoveAcross().toString()
    }

    // Elves take all the presents from the person to their left
    // This function finds the elf who'll be the last man standing
    private fun findLastElfRemoveLeft(): Int {
        var currentElf = this.elves
        // It's a circle, the current elf needs to start pointing to elf after its next
        // Because the next elf loses its presents
        while (currentElf.number != currentElf.next!!.number) {
            currentElf.next = currentElf.next!!.next
            currentElf = currentElf.next!!
        }
        return currentElf.number
    }

    // Elves take the presents from the elf from across the circle
    // So we have deques for the left and right side of the circle, taking out the last elf from the larger one
    // If equal, take from the right one
    // The queues are built from the starting point to their respective direction, if we think of a clock
    // in the left side there's 1, 2, 3 and on the right side, going from 12, there's 11, 10, 9
    private fun findLastElfRemoveAcross(): Int {
        while (leftSideCircle.size + rightSideCircle.size > 1) {
            if (leftSideCircle.size > rightSideCircle.size) leftSideCircle.removeLast()
            else rightSideCircle.removeLast()
            // Rearrange the circle, the first element from the left now becomes the first element in the right side
            // and the last element from the left becomes the last element in the right, maintaining the 'clock' order
            rightSideCircle.addFirst(leftSideCircle.removeFirst())
            leftSideCircle.addLast(rightSideCircle.removeLast())
        }
        // There's only one element left, this gives it
        return leftSideCircle.sum() + rightSideCircle.sum()
    }
}

private class Day19Parser : IParser

private class LinkedElf(val number: Int, var next: LinkedElf?)