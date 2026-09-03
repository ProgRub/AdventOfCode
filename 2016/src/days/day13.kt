package days

import java.util.*
import kotlin.math.abs

class Day13 : IDaySolver, IAStarAlgorithm {
    override var parser: IParser = Day13Parser()
    override var problemInput: List<String>
    private val startingPoint = Pair(1, 1)
    private val targetPoint = Pair(31, 39)
    private val numberToAdd: Int

    // Problem input is just one line with a value key to the problem
    constructor(input: List<String>) {
        this.problemInput = input
        numberToAdd = input[0].toInt()
    }

    override fun test(): String {
        return findShortestPathLength(this.startingPoint, this.targetPoint).toString()
    }

    // Part 1 is to find optimal path to the target point
    override fun part1(): String {
        return findShortestPathLength(this.startingPoint, this.targetPoint).toString()
    }

    // Part 2 is to find how many different points we can hit in 50 steps
    override fun part2(): String {
        val distinctPoints = findDistinctPoints(this.startingPoint, 50, setOf())
        return distinctPoints.size.toString()
    }

    // Print grid to visualize what's a wall or not
    private fun printGrid(maxX: Int, maxY: Int, path: Collection<Pair<Int, Int>>) {
        for (indeY in 0..maxY) {
            var rowString = "$indeY\t"
            for (indeX in 0..maxX) {
                rowString += when {
                    isWall(indeX to indeY) -> "#"
                    indeX to indeY == targetPoint -> "X"
                    indeX to indeY in path -> "O"
                    else -> "."
                }
            }
            println(rowString)
        }
    }

    // Determines if the point passed is a wall or not
    // A point is a wall if the binary representation of the result has an odd number of 1's
    private fun isWall(point: Pair<Int, Int>): Boolean {
        val (x, y) = point
        val number = x * x + 3 * x + 2 * x * y + y + y * y + numberToAdd
        return number.countOneBits() % 2 != 0
    }

    // A Dijkstra algorithm to find how many different points we can hit given a max amount of steps to take
    // and a starting point
    private fun findDistinctPoints(
        startingPoint: Pair<Int, Int>,
        maxSteps: Int,
        pointsVisited: Set<Pair<Int, Int>>
    ): MutableSet<Pair<Int, Int>> {
        val visitedPoints = pointsVisited.toMutableSet()
        // We need to find the amount of steps taken optimally back to the start so we consider the optimal path's
        // to the max amount of steps allowed
        val stepsTakenSoFar = findShortestPathLength(this.startingPoint, startingPoint)
        if (maxSteps - stepsTakenSoFar > 0) {
            // If the current point was already visited before no point in checking, it's already in the set
            if (!visitedPoints.add(startingPoint)) return visitedPoints
            val possiblePoints = getPossiblePoints(startingPoint)
            for (point in possiblePoints) {
                visitedPoints.addAll(
                    findDistinctPoints(point, maxSteps, visitedPoints)
                )
                visitedPoints.add(point)
            }
        }
        return visitedPoints
    }

    // An A* algorithm to find the shortest path possible from the starting point to the target point
    // Dynamically checking if the next point is a wall or not
    override fun findShortestPathLength(startingPoint: Pair<Int, Int>, destinationPoint: Pair<Int, Int>): Int {
        var currentPoint = startingPoint
        val visitedPoints = mutableSetOf<Pair<Int, Int>>()
        var costSoFar = 0
        val path = Stack<Pair<Int, Int>>()
        val pathCosts = mutableMapOf<Pair<Int, Int>, Int>()
        while (currentPoint != destinationPoint) {
            visitedPoints.add(currentPoint)
            path.push(currentPoint)
            pathCosts[currentPoint] = costSoFar
            var possiblePoints = getPossiblePoints(currentPoint)
            // Filter out the points that we have already visited because they lead to dead ends
            possiblePoints = possiblePoints.filter { !visitedPoints.contains(it) }
            // If there is a move to make, make the best one
            if (possiblePoints.isNotEmpty()) {
                val (nextPoint, pathCost) = chooseNextPoint(possiblePoints, costSoFar, destinationPoint)
                currentPoint = nextPoint
                costSoFar += pathCost
            }
            // Otherwise we're surrounded by walls, we need to go back
            else {
                if (path.peek() == currentPoint) {
                    pathCosts.remove(path.pop()) // Remove the point that is a dead end
                }
                currentPoint = path.pop() // Go back to point before that
                pathCosts.remove(currentPoint)
            }
        }
        return path.size
    }

    // Get a list of possible points to move to form the given point, can't move diagonally
    override fun getPossiblePoints(point: Pair<Int, Int>): List<Pair<Int, Int>> {
        val possiblePoints = mutableListOf<Pair<Int, Int>>()
        val (x, y) = point
        // Can't go into negative coordinates or into a wall
        if (x - 1 >= 0 && !isWall(x - 1 to y)) possiblePoints.add(x - 1 to y)
        if (y - 1 >= 0 && !isWall(x to y - 1)) possiblePoints.add(x to y - 1)
        if (!isWall(x + 1 to y)) possiblePoints.add(x + 1 to y)
        if (!isWall(x to y + 1)) possiblePoints.add(x to y + 1)
        return possiblePoints
    }

    // Determine what point to go to for the shortest path, returning it and the cost to make the move
    override fun chooseNextPoint(
        possiblePoints: List<Pair<Int, Int>>,
        pathCost: Int,
        destination: Pair<Int, Int>
    ): Pair<Pair<Int, Int>, Int> {
        var bestPoint = possiblePoints[0]
        var costBestMove = pathCost +
                (abs(destination.component1() - bestPoint.component1()) +
                        abs(destination.component2() - bestPoint.component2()))
        for (point in possiblePoints.subList(1, possiblePoints.size)) {
            val moveCost = pathCost +
                    (abs(destination.component1() - point.component1()) +
                            abs(destination.component2() - point.component2()))
            if (moveCost < costBestMove) {
                costBestMove = moveCost
                bestPoint = point
            }
            // Edge case tiebreaker, avoid going around in a circle
            else if (moveCost == costBestMove) {
                val possiblePointsBest = getPossiblePoints(bestPoint)
                val possiblePointsCurrent = getPossiblePoints(point)
                val common = possiblePointsCurrent.filter { possiblePointsBest.contains(it) }.toMutableSet()
                common.add(bestPoint)
                common.add(point)
                val difference = possiblePointsCurrent.filter { !possiblePointsBest.contains(it) }
                // If the possible points in common between the best and current point being checked
                // along with the best point and current point is 4 then it's a circle, we take the current point
                // as the best if it has new paths to explore because there's no point in going around a circle
                if (common.size == 4 && difference.isNotEmpty()) {
                    bestPoint = point
                }
            }
        }
        return bestPoint to (costBestMove - pathCost)
    }
}

private class Day13Parser : IParser