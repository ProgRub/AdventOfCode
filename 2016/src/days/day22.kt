package days

import kotlin.math.abs

class Day22 : IDaySolver {
    override var parser: IParser = Day22Parser()
    override var problemInput: List<String>
    private val nodes: List<Node>
    private val rowSize: Int
    private val colSize: Int

    // Input is the terminal with a df command ran, with a list of nodes and their usage
    constructor(input: List<String>) {
        this.problemInput = input
        this.nodes = problemInput.subList(2, input.size).map { Node(it) }
        this.rowSize = nodes.maxOf { it.x } + 1
        this.colSize = nodes.maxOf { it.y } + 1
    }

    override fun test(): String {
        return nodes.size.toString()
    }

    override fun part1(): String {
        return findAmountPairs(nodes).toString()
    }

    override fun part2(): String {
        return findOptimalAmountSteps(nodes).toString()
    }

    // Find the amount of viable pairs of nodes: for a pair of nodes, A and B, to be viable:
    // Node A is not empty (used != 0)
    // A is different from B
    // B has available space for the data in A (B's availability is greater or equal than A's used)
    private fun findAmountPairs(nodes: List<Node>): Int {
        // Have to lists of nodes sorted by used and availability to make it easier to stop checking
        val nodesSortedUsed = nodes.sortedBy { it.used }
        val nodesSortedAvail = nodes.sortedByDescending { it.available }
        var pairs = 0
        for (nodeA in nodesSortedUsed) {
            if (nodeA.used == 0) continue // If node A has no data it doesn't count
            for (nodeB in nodesSortedAvail) {
                if (nodeA == nodeB) continue // Can't move data into the same node
                // Since the nodes are sorted, once we hit one that can't take node A's data we can stop
                if (nodeB.available < nodeA.used) break
                pairs++
            }
        }
        return pairs
    }

    // This function finds the optimal solution to move the data from the node in the top right corner
    // to the point (0,0)
    // First we find the empty node -> get this empty node adjacent to the node that's being moved ->
    // -> moving the data to the empty node
    // After getting the empty node on row 0, it's just a cycle of 5 steps:
    // move the empty node back around -> move data -> move the empty node back around
    // This cycle needs to be done the amount of times it takes from the x of the node with data to 0
    private fun findOptimalAmountSteps(nodes: List<Node>): Int {
        var steps = 0
        val originNode = nodes.first() // Node (0,0)
        val dataNode = nodes[(rowSize - 1) * colSize]
        var emptyNode = nodes.first { it.used == 0 }
        // There is a wall of nodes that can't be moved at y=27
        // So we first move the empty node to the closest available node at y=27 to then bring it to the data node
        val roadblocksWall = nodes.filter { it.used > 200 }
        val wallCrackPointLeft = roadblocksWall.first().x - 1 to roadblocksWall.first().y
        val wallCrackPointRight = roadblocksWall.last().x + 1 to roadblocksWall.first().y
        // Find the point at y=27 where the empty node can go, for minimum amount of steps
        val wallCrackPoint = when {
            // If the wall goes to the last column, then the crack in the wall is on the left
            wallCrackPointRight.component1() + 1 >= rowSize -> wallCrackPointLeft
            // If the wall goes to the first column, crack on the right
            wallCrackPointLeft.component1() - 1 < 0 -> wallCrackPointRight
            // If the left crack is closer use that
            abs(emptyNode.x - wallCrackPointLeft.component1()) <= abs(emptyNode.x - wallCrackPointRight.component1()) -> wallCrackPointLeft
            // Otherwise use the right crack
            else -> wallCrackPointRight
        }
        // Add the steps needed to take the empty node to the crack in the wall
        steps += findShortestPathLength(emptyNode.x to emptyNode.y, wallCrackPoint, nodes)
        emptyNode = nodes[wallCrackPoint.component1() * colSize + wallCrackPoint.component2()] // Update empty node
        // The target for the empty node is to be in the same row as the data node, but one column closer
        val targetPosition = dataNode.x - 1 to dataNode.y
        // Add the amount of steps needed to get the empty node to the same row as the data node
        val stepsTaken =
            findShortestPathLength(emptyNode.x to emptyNode.y, targetPosition, nodes)
        steps += stepsTaken + 1 // +1 for the step of moving the data to the empty node
        // Now we just need the add the amount of steps in a cycle (5) times the amount of cycles (distance to (0,0))
        steps += 5 * (targetPosition.component1() - originNode.x)
        return steps
    }

    // An A* algorithm to find the shortest path possible from the starting point to the target point
    // A move is only viable if the data between the nodes can be moved
    // Returns the amount of steps taken
    private fun findShortestPathLength(
        startingPoint: Pair<Int, Int>,
        destinationPoint: Pair<Int, Int>,
        nodes: List<Node>
    ): Int {
        var currentPoint = startingPoint
        var currentNode = nodes[currentPoint.component1() * colSize + currentPoint.component2()]
        var steps = 0
        while (currentPoint != destinationPoint) {
            val possiblePoints = getPossiblePoints(currentNode, nodes)
            val nextPoint = chooseNextPoint(possiblePoints, destinationPoint)
            val nextNode = nodes[nextPoint.component1() * colSize + nextPoint.component2()]
            // Move the data between the nodes
            currentNode.used = nextNode.used
            currentNode.available = currentNode.size - currentNode.used
            nextNode.used = 0
            nextNode.available = nextNode.size
            steps++
            // Next iteration
            currentPoint = nextPoint
            currentNode = nextNode
        }
        return steps
    }

    // Get a list of possible points to move to form the given point, can't move diagonally
    private fun getPossiblePoints(currentNode: Node, nodes: List<Node>): List<Pair<Int, Int>> {
        val possiblePoints = mutableListOf<Pair<Int, Int>>()
        val (x, y) = currentNode.x to currentNode.y
        // Can't go outside the grid
        if (x - 1 >= 0) possiblePoints.add(x - 1 to y)
        if (y - 1 >= 0) possiblePoints.add(x to y - 1)
        if (x + 1 < rowSize) possiblePoints.add(x + 1 to y)
        if (y + 1 < colSize) possiblePoints.add(x to y + 1)
        // Now filter out the nodes whose data can't be moved
        val possiblePointsFiltered = mutableListOf<Pair<Int, Int>>()
        for ((x, y) in possiblePoints) {
            val possibleNode = nodes[x * colSize + y]
            // Point is only valid if the data can be moved between the nodes, and there's data to move
            if ((currentNode.available >= possibleNode.used && possibleNode.used != 0) || currentNode.used <= possibleNode.available && currentNode.used != 0)
                possiblePointsFiltered.add(x to y)
        }
        return possiblePointsFiltered
    }

    // Determine what point to go to for the shortest path, returning it
    private fun chooseNextPoint(
        possiblePoints: List<Pair<Int, Int>>,
        destination: Pair<Int, Int>
    ): Pair<Int, Int> {
        var bestPoint = possiblePoints[0]
        var costBestMove =
            abs(destination.component1() - bestPoint.component1()) +
                    abs(destination.component2() - bestPoint.component2())
        for (point in possiblePoints.subList(1, possiblePoints.size)) {
            val moveCost =
                abs(destination.component1() - point.component1()) +
                        abs(destination.component2() - point.component2())
            if (moveCost < costBestMove) {
                costBestMove = moveCost
                bestPoint = point
            }
            // Tiebreaker case, prioritize going left
            if (moveCost == costBestMove)
                bestPoint = if (point.component1() < bestPoint.component1()) point else bestPoint
        }
        return bestPoint
    }
}

private class Day22Parser : IParser

private class Node {
    val x: Int
    val y: Int
    var size: Int
    var used: Int
    var available: Int
    var usePercentage: Int

    constructor(description: String) {
        val split = description.split("\\s+".toRegex())
        val splitName = split[0].split('-')
        x = splitName[1].substring(1).toInt()
        y = splitName[2].substring(1).toInt()
        size = split[1].trim { it == 'T' }.toInt()
        used = split[2].trim { it == 'T' }.toInt()
        available = split[3].trim { it == 'T' }.toInt()
        usePercentage = split[4].trim { it == '%' }.toInt()
    }

    override fun equals(other: Any?): Boolean = other is Node && x == other.x && y == other.y
    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + size
        result = 31 * result + used
        result = 31 * result + available
        result = 31 * result + usePercentage
        return result
    }

}