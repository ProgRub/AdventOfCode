package days

import java.security.MessageDigest
import kotlin.math.min

class Day17 : IDaySolver {
    override var parser: IParser = Day17Parser()
    override var problemInput: List<String>
    private val messageDigest = MessageDigest.getInstance("MD5")
    private val gridSize = 4
    private val vaultPosition = gridSize - 1 to gridSize - 1
    private var lengthShortestPath = Int.MAX_VALUE
    private val paths = mutableListOf<String>()

    // Input is just the initial password
    constructor(input: List<String>) {
        this.problemInput = input
    }

    override fun test(): String {
        findShortestPath(this.problemInput[0], 0 to 0, vaultPosition, "", 0)
        return paths.first { it.length == lengthShortestPath }
    }

    // For part 1 we want the shortest path
    override fun part1(): String {
        findShortestPath(this.problemInput[0], 0 to 0, vaultPosition, "", 0)
        return paths.first { it.length == lengthShortestPath }
    }

    // For part 2 we want the length of the longest path, so we find all paths to then find the longest
    override fun part2(): String {
        findAllPaths(this.problemInput[0], 0 to 0, vaultPosition, "")
        return paths.maxOf { it.length }.toString()
    }

    // Recursive function that finds the shortest possible path to the target
    // It stores paths found in the class's list and updates the length of the shortest path
    // To avoid exploring unnecessary paths
    private fun findShortestPath(
        password: String,
        initialPosition: Pair<Int, Int>,
        targetPosition: Pair<Int, Int>,
        currentPath: String,
        stepsTaken: Int
    ) {
        if (initialPosition == targetPosition) {
            this.lengthShortestPath = min(lengthShortestPath, stepsTaken)
            paths.add(currentPath)
            return
        }
        // If taking another step takes us over the shortest path no point in checking
        if (stepsTaken + 1 > this.lengthShortestPath) return
        // Explore the possible open doors of the password
        val possibleDoors = findOpenDoors(initialPosition, password)
        for (door in possibleDoors) {
            val newPosition = findNewPositionAfterDoor(initialPosition, door)
            // Add the door to the password and current path, 1 step was taken
            findShortestPath(password + door, newPosition, targetPosition, currentPath + door, stepsTaken + 1)
        }
        return
    }

    // Recursive function that finds all possible paths to target, storing it in the class's list
    private fun findAllPaths(
        password: String,
        initialPosition: Pair<Int, Int>,
        targetPosition: Pair<Int, Int>,
        currentPath: String
    ) {
        if (initialPosition == targetPosition) {
            paths.add(currentPath)
            return
        }
        // Explore the possible open doors of the password
        val possibleDoors = findOpenDoors(initialPosition, password)
        for (door in possibleDoors) {
            val newPosition = findNewPositionAfterDoor(initialPosition, door)
            // Add the door to the password and current path
            findAllPaths(password + door, newPosition, targetPosition, currentPath + door)
        }
        return
    }

    // Returns the new position after going through the door specified based on current position
    private fun findNewPositionAfterDoor(position: Pair<Int, Int>, door: Char): Pair<Int, Int> {
        return when (door) {
            'U' -> position.component1() to position.component2() - 1
            'D' -> position.component1() to position.component2() + 1
            'L' -> position.component1() - 1 to position.component2()
            'R' -> position.component1() + 1 to position.component2()
            else -> -1 to -1 // impossible
        }
    }

    // Given current position and current state of the password, hashes the password and checks the first
    // 4 characters in the resulting hash to check which doors are open, by order of up, down, left right
    // A door is considered open if its character is b, c, d, e or f aka, greater than 10
    // We take the position to filter out the impossible doors, for example the up door when we're in the first row
    private fun findOpenDoors(position: Pair<Int, Int>, currentPassword: String): String {
        var openDoors = ""
        val possibleValues = mutableSetOf('b', 'c', 'd', 'e', 'f')
        val hashedValue = messageDigest.digest(currentPassword.toByteArray()).toHexString()
        val (x, y) = position
        if (y > 0 && hashedValue[0] in possibleValues) openDoors += "U" // Up door is available
        if (y < gridSize - 1 && hashedValue[1] in possibleValues) openDoors += "D" // Down door is available
        if (x > 0 && hashedValue[2] in possibleValues) openDoors += "L" // Left door is available
        if (x < gridSize - 1 && hashedValue[3] in possibleValues) openDoors += "R" // Right door is available
        return openDoors
    }
}

private class Day17Parser : IParser