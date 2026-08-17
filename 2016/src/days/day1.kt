package days

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Day1 : IDaySolver {
    override var parser: IParser = Day1Parser()
    override var problemInput: List<String>
    val errorPoint = listOf(Int.MIN_VALUE, Int.MIN_VALUE)

    // Input data consists of a list of Lx and Rx entries, where x is an int
    // Signifying we turn left or right, respectively, and walk the number of blocks specified by x
    constructor(input: List<String>) {
        this.problemInput = this.parser.parseTextToList(input[0], ",")
    }
    override fun test(): String {
        return calculateDistance().toString()
    }
    override fun part1(): String {
        return calculateDistance().toString()
    }
    override fun part2(): String {
        val crossoverPoint = findFirstPointVisitedTwice()
        return calculateDistance(crossoverPoint).toString()
    }

    // Determine next point given the current orientation and point, making the move specified
    private fun nextPoint(currentPoint: List<Int>, orientation: Orientation, nextMove: String): List<Int> {
        val direction = nextMove[0]
        val steps = nextMove.substring(1).toInt()
        val newOrientation = findNextOrientation(orientation, direction)
        return listOf(
            when (newOrientation) {
                Orientation.EAST -> currentPoint[0] + steps
                Orientation.WEST -> currentPoint[0] - steps
                else -> currentPoint[0]
            }, when (newOrientation) {
                Orientation.NORTH -> currentPoint[1] + steps
                Orientation.SOUTH -> currentPoint[1] - steps
                else -> currentPoint[1]
            }
        )
    }

    // Calculates the distance from 0,0 to point given
    private fun calculateDistance(point: List<Int>): Int = abs(point[0]) + abs(point[1])

    // Calculates the distance from starting point to destination in blocks
    private fun calculateDistance(): Int {
        var xDistance = 0
        var yDistance = 0
        var orientation = Orientation.NORTH
        var currentPoint = listOf(xDistance, yDistance)
        var nextPoint: List<Int>
        for (move in problemInput) {
            nextPoint = nextPoint(currentPoint, orientation,move)
            orientation = findNextOrientation(orientation,move[0])
            xDistance += currentPoint[0] - nextPoint[0]
            yDistance += currentPoint[1] - nextPoint[1]
            currentPoint = nextPoint
        }
        return abs(xDistance) + abs(yDistance)
    }

    // Traces out the points visited until we visit the same point twice, returning said point
    private fun findFirstPointVisitedTwice(): List<Int> {
        val points = mutableListOf<List<Int>>()
        var orientation = Orientation.NORTH
        var currentPoint = listOf(0, 0)
        var nextPoint: List<Int>
        for (move in problemInput) {
            nextPoint = nextPoint(currentPoint, orientation,move)
            orientation = findNextOrientation(orientation,move[0])
            points.add(nextPoint)
            // Check if crossover has happened
            val crossoverPoint = crossoverPoint(points)
            if (crossoverPoint != errorPoint) return crossoverPoint
            currentPoint = nextPoint
        }
        return points.last()
    }

    // Determines if crossover has happened, comparing the movement lines
    // Crossover happens if the last move line crosses over with any previous movement line
    // We calculate the last line made, from penultimate and last points, and compare this to all previous lines made
    // If there's crossover we return the point where thei crossover
    private fun crossoverPoint(points: List<List<Int>>): List<Int> {
        if (points.size < 4) return errorPoint // Crossover can't happen without 4 moves
        val lastPoint = points.last()
        val penultimatePoint = points[points.size - 2]
        var startPoint: List<Int>
        var endPoint = listOf(0, 0)
        for (point in points.subList(0, points.size - 1)) { // Can't check the last point
            startPoint = endPoint
            endPoint = point
            // No crossover if lines are parallel
            if (lastPoint[0] == penultimatePoint[0] && startPoint[0] == endPoint[0]) {
                continue
            }
            if (lastPoint[1] == penultimatePoint[1] && startPoint[1] == endPoint[1]) {
                continue
            }
            // If the X points are the same, then it's a line on the Y-axis
            if (lastPoint[0] == penultimatePoint[0]) {
                // First we check if the y value of the line made by start point and end point
                // falls in between the start and end of the last line
                if ((startPoint[1] > penultimatePoint[1] && startPoint[1] < lastPoint[1])
                    // If it does, we check if the X-axis line made by start and end points intersects with the last line
                    // The smallest point from the start-end line needs to be to the left of the X-value of the last line
                    // and the biggest point needs to the right of the X-value
                    && (min(startPoint[0], endPoint[0]) < lastPoint[0] &&
                            max(startPoint[0], endPoint[0]) > lastPoint[0])
                ) return listOf(lastPoint[0], startPoint[1])
            }
            // If the Y points are the same, then it's a line on the X-axis
            if (lastPoint[1] == penultimatePoint[1]) {
                // First we check if the x value of the line made by start point and end point
                // falls in between the start and end of the last line
                if ((startPoint[0] > penultimatePoint[0] && startPoint[0] < lastPoint[0])
                    // If it does, we check if the Y-axis line made by start and end points intersects with the last line
                    // The smallest point from the start-end line needs to be below the Y-value of the last line
                    // and the biggest point needs to be above the Y-value
                    && (min(startPoint[1], endPoint[1]) < lastPoint[1] &&
                            max(startPoint[1], endPoint[1]) > lastPoint[1])
                ) return listOf(startPoint[0], lastPoint[1])
            }
        }
        return errorPoint
    }

    // Determines next orientation based on current orientation and next direction
    private fun findNextOrientation(current: Orientation, moveDirection: Char): Orientation {
        if (moveDirection == 'R') { // We're moving right
            return when (current) {
                Orientation.NORTH -> Orientation.EAST
                Orientation.EAST -> Orientation.SOUTH
                Orientation.SOUTH -> Orientation.WEST
                Orientation.WEST -> Orientation.NORTH
            }
        }
        return when (current) { //We're moving left
            Orientation.NORTH -> Orientation.WEST
            Orientation.WEST -> Orientation.SOUTH
            Orientation.SOUTH -> Orientation.EAST
            Orientation.EAST -> Orientation.NORTH
        }
    }
}

class Day1Parser : IParser

enum class Orientation { NORTH, SOUTH, EAST, WEST }