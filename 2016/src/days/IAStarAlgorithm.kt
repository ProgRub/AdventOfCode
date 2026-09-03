package days

internal interface IAStarAlgorithm {
    // An A* algorithm to find the shortest path possible from the starting point to the target point
    fun findShortestPathLength(startingPoint: Pair<Int, Int>, destinationPoint: Pair<Int, Int>): Int

    // Get a list of possible points to move to form the given point, can't move diagonally
    fun getPossiblePoints(point: Pair<Int, Int>): List<Pair<Int, Int>>

    // Determine what point to go to for the shortest path, returning it and the cost to make the move
    fun chooseNextPoint(
        possiblePoints: List<Pair<Int, Int>>,
        pathCost: Int,
        destination: Pair<Int, Int>
    ): Pair<Pair<Int, Int>, Int>
}