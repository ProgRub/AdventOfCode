package days

import java.util.*

class Day11 : IDaySolver {
    override var parser: IParser = Day11Parser()
    override var problemInput: List<String>
    private var floors: MutableList<MutableList<RadiationObject>>
    private var totalFloors: Int
    private val states: MutableSet<String>
    private val queue: LinkedList<State>

    // The input is a set of lines where each line says what objects are on the floor
    constructor(input: List<String>) {
        this.problemInput = input
        floors = mutableListOf()
        totalFloors = this.problemInput.size
        repeat(totalFloors) { floors += mutableListOf<RadiationObject>() }
        states = mutableSetOf<String>()
        queue = LinkedList<State>()
    }

    override fun test(): String {
        populateFloors()
        return minStepsToTopFloor(this.floors).toString()
    }

    override fun part1(): String {
        populateFloors()
        return minStepsToTopFloor(this.floors).toString()
    }

    // Input changes from part1, adds 2 chips and 2 generators to the first floor
    override fun part2(): String {
        populateFloors()
        return minStepsToTopFloor(this.floors).toString()
    }

    // Decode the text to find what objects are on what floors
    private fun populateFloors() {
        var floorIndex = -1
        for (line in this.problemInput) {
            floorIndex++
            // Floor has nothing in it
            if (line.endsWith("nothing relevant.")) continue
            var split = line.split(' ')
            split = split.slice(5..<split.size).filter { it != "and" && it != "a" }
            for (index in 0 until split.size step 2) {
                this.floors[floorIndex] += RadiationObject(split[index] + " " + split[index + 1])
            }
        }
    }

    private fun printFloors(floors: MutableList<MutableList<RadiationObject>>) {
        var number = 1
        for (floor in floors) {
            println("Floor $number: ${floor.joinToString(";")}")
            number++
        }
        println("${"-".repeat(50)}")
    }

    // Check if the floor is valid
    // A floor is only valid if it has no generators or all the generators have their equivalent microchip
    private fun isValidFloor(items: List<RadiationObject>): Boolean {
        val generators = items.filter { it.type == ObjectType.GENERATOR }
        return generators.isEmpty() || items.filter { it.type == ObjectType.MICROCHIP }
            .all { it.element in generators.map { it.element } }
    }

    // Elements are interchangeable for validity purposes, so we key on the sorted
    // multiset of (chipFloor, genFloor) pairs rather than which element is which.
    // This massively shrinks the visited set
    private fun canonicalKey(elevator: Int, floors: MutableList<MutableList<RadiationObject>>): String {
        val elements = floors.flatten().map { it.element }.toSet()
        val pairs = elements.map { el ->
            val chipFloor = floors.indexOfFirst { f -> f.any { it.type == ObjectType.MICROCHIP && it.element == el } }
            val genFloor = floors.indexOfFirst { f -> f.any { it.type == ObjectType.GENERATOR && it.element == el } }
            chipFloor to genFloor
        }
        return "$elevator|$pairs"
    }

    // Finds the minimum amount of steps (elevator rides) to get all objects to the top floor
    private fun minStepsToTopFloor(initialFloors: MutableList<MutableList<RadiationObject>>): Int {
        // Copy initial floors
        val floors = initialFloors.map { floor -> floor.map { it }.toMutableList() }.toMutableList()
        val totalItems = floors.sumOf { it.size }
        val topFloor = this.totalFloors - 1

        val start = State(0, floors) // Starting state
        // Set of visited states
        val visited = HashSet<String>().apply { add(canonicalKey(start.elevator, start.floors)) }

        // Queue that connects states with how many steps it took to get there
        val queue = ArrayDeque<Pair<State, Int>>()
        queue.add(start to 0)

        while (queue.isNotEmpty()) {
            val (state, steps) = queue.poll()
            // printFloors(state.floors) // Help visualizing current state
            // If the amount of items on the top floor equals the amount of total items then we're finished
            if (state.floors[topFloor].size == totalItems) return steps

            val currentFloor = state.floors[state.elevator] // current floor where elevator is
            // Find all the ways we can fill the elevator, either with one object or 2
            val combos = mutableListOf<List<RadiationObject>>()
            for (i in currentFloor.indices) {
                combos.add(mutableListOf(currentFloor[i]))
                for (j in i + 1..<currentFloor.size) combos.add(mutableListOf(currentFloor[i], currentFloor[j]))
            }

            // Elevator can only go up or down a floor at a time
            for (direction in intArrayOf(1, -1)) {
                val nextElevatorFloor = state.elevator + direction
                if (nextElevatorFloor !in floors.indices) continue // Elevator can't go past bottom and top floors

                // Go through every possible way we can fill the elevator
                for (combo in combos) {
                    val updatedFloor = currentFloor - combo // Remove the elements we're taking up
                    // Add the elements we're taking to the target floor
                    val updatedTargetFloor = state.floors[nextElevatorFloor] + combo
                    // If either of the floors are now invalid this combo is invalid, check the next one
                    if (!isValidFloor(updatedFloor) || !isValidFloor(updatedTargetFloor)) continue

                    // Need to create a copy of the current state's floors and update the floors affected
                    val newFloors = state.floors.map { it.toMutableList() }.toMutableList()
                    newFloors[state.elevator] = updatedFloor.toMutableList()
                    newFloors[nextElevatorFloor] = updatedTargetFloor.toMutableList()

                    // Add a new state consisting of the new elevator position, new arrangement of the floors and
                    // updated step count
                    val key = canonicalKey(nextElevatorFloor, newFloors)
                    // We only add state to the queue if it hasn't been previously visited
                    if (visited.add(key)) queue.add(State(nextElevatorFloor, newFloors) to steps + 1)
                }
            }
        }
        return -1 // unreachable for a solvable puzzle
    }

}

class Day11Parser : IParser

internal class RadiationObject {
    val type: ObjectType
    val element: String

    constructor(text: String) {
        val strippedText = text.trim { it -> it == ',' || it == '.' }
        val split = strippedText.split(' ')
        this.type = when (split[1]) {
            "microchip" -> ObjectType.MICROCHIP
            else -> ObjectType.GENERATOR
        }
        this.element = when (type) {
            ObjectType.MICROCHIP -> split[0].substring(0, split[0].indexOf('-')).replaceFirstChar(Char::uppercase)
            else -> split[0].replaceFirstChar(Char::uppercase)
        }
    }

    override fun toString(): String {
        return when (type) {
            ObjectType.MICROCHIP -> "$element Microchip"
            else -> "$element Generator"
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is RadiationObject && type == other.type && element == other.element
    }

    override fun hashCode(): Int {
        return this.type.ordinal * this.element.hashCode()
    }
}

internal enum class ObjectType { MICROCHIP, GENERATOR }

internal data class State(val elevator: Int, val floors: MutableList<MutableList<RadiationObject>>)