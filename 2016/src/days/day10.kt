package days

import java.util.*
import kotlin.math.max

class Day10 : IDaySolver {
    override var parser: IParser = Day10Parser()
    override var problemInput: List<String>
    lateinit var targetValuesList: List<Int>
    private val botsList: MutableList<Bot>
    private val outputStacks: MutableMap<Int, Stack<Int>> = mutableMapOf()

    // The input is a list of moves to be made
    constructor(input: List<String>) {
        this.problemInput = input
        this.botsList = mutableListOf()
    }

    override fun test(): String {
        return findBotTargetValues().toString()
    }

    // We want the bot that holds the target values
    override fun part1(): String {
        targetValuesList = listOf<Int>(17, 61)
        return findBotTargetValues().toString()
    }

    // We want the product of the chips in output's 0, 1 and 2
    override fun part2(): String {
        targetValuesList = listOf<Int>(-1, -1) // We don't want to find a bot, we want to make all moves
        getBotsAndOutputs()
        makeAllMoves()
        val targetStacks = outputStacks.filter { it.key < 3 }.values
        var product = 1
        for (stack in targetStacks) {
            while (stack.isNotEmpty()) product *= stack.pop()
        }
        return product.toString()
    }

    private fun makeAllMoves() {
        // Get the list of bots that have 2 values at the beginning
        var botsFilled = this.botsList.filter { it.values.all { it != 0 } }
        val moves = this.problemInput.filter { !it.startsWith("value") }.toMutableList()
        // While we have moves to make, we make them
        while (moves.isNotEmpty()) {
            makeMove(botsFilled.first(), moves)
            botsFilled = this.botsList.filter { it.values.all { it != 0 } }
        }
    }

    // Build the list of bots with their initial values, and map of outputs from the list of moves
    private fun getBotsAndOutputs() {
        for (move in this.problemInput) {
            val split = move.split(' ')
            when (move.startsWith("value")) {
                // Example: value 5 goes to bot 2
                true -> {
                    val bot = addBot(split.last().toInt())
                    bot.receiveValue(split[1].toInt())
                }
                // Example: bot 1 gives low to bot 3 and high to bot 0
                else -> {
                    addBot(split[1].toInt())
                    if (split[5] == "bot") addBot(split[6].toInt())
                    else this.outputStacks[split[6].toInt()] = Stack()
                    if (split[10] == "bot") addBot(split[11].toInt())
                    else this.outputStacks[split[11].toInt()] = Stack()
                }
            }
        }
        this.botsList.sortBy { it.id }
    }

    // Add bot to the list if it isn't in it
    private fun addBot(botId: Int): Bot {
        var newBot = Bot(botId, mutableListOf(0, 0))
        if (!botsList.any { it.id == botId }) botsList += newBot
        else newBot = this.botsList.first { it.id == botId }
        return newBot
    }

    // Find move that the bot can make, make it and do the same for the bots that end up with 2 values
    // When we reach the bot that holds the values we are targeting, we can stop
    private fun makeMove(givingBot: Bot, moves: MutableList<String>): Int {
        if (givingBot.checkValues(this.targetValuesList)) return givingBot.id
        val lowValue = givingBot.values[0]
        val highValue = givingBot.values[1]
        val nextMove = try {
            moves.first {
                it.startsWith("bot " + givingBot.id + " ")
            }
        } catch (_: NoSuchElementException) {
            return 0 // No more moves to make for the bot, exit recursion
        }
        moves -= nextMove
        val split = nextMove.split(' ')
        var targetBotId = 0
        lateinit var botReceivedLow: Bot
        lateinit var botReceivedHigh: Bot
        // First we give the low value
        var id = split[6].toInt()
        when (split[5]) {
            "bot" -> {
                botReceivedLow = botsList[id]
                givingBot.giveValue(lowValue, botReceivedLow)
                // If the receiving bot now has 2 values, make its next move
                if (botReceivedLow.values.all { it != 0 })
                    targetBotId = max(makeMove(botReceivedLow, moves), targetBotId)
            }

            else -> givingBot.giveValue(lowValue, this.outputStacks[id])
        }
        // Then we give the high value
        id = split[11].toInt()
        when (split[10]) {
            "bot" -> {
                botReceivedHigh = botsList[id]
                givingBot.giveValue(highValue, botReceivedHigh)
                // If the receiving bot now has 2 values, make its next move
                if (botReceivedHigh.values.all { it != 0 })
                    targetBotId = max(makeMove(botReceivedHigh, moves), targetBotId)
            }

            else -> givingBot.giveValue(highValue, this.outputStacks[id])
        }

        return targetBotId
    }

    // Find bot that ends up with target list of values
    private fun findBotTargetValues(): Int {
        getBotsAndOutputs()
        // Get the list of bots that have 2 values at the beginning
        val botsFilled = this.botsList.filter { it.values.all { it != 0 } }
        val moves = this.problemInput.filter { !it.startsWith("value") }.toMutableList()
        // We check the moves these bots can make
        var targetBotId = 0
        while (targetBotId == 0) {
            targetBotId = makeMove(botsFilled.first(), moves)
        }
        return targetBotId
    }
}

class Day10Parser : IParser

private class Bot(val id: Int, val values: MutableList<Int>) {
    // Bot gets a value from a 'value x goes to bot' move
    fun receiveValue(value: Int) {
        // The bot always needs to have one space available, that space being the first position, filled with 0
        values[0] = value
        reorderValues()
    }

    // Bot gives a value to another bot
    fun giveValue(value: Int, receivingBot: Bot) {
        val indexOfValue = values.indexOf(value)
        receivingBot.receiveValue(value)
        values[indexOfValue] = 0
        reorderValues()
    }

    // Bot gives a value to an output stack
    fun giveValue(value: Int, output: Stack<Int>?) {
        val indexOfValue = values.indexOf(value)
        output?.add(value)
        values[indexOfValue] = 0
        reorderValues()
    }

    // Order list of values of the bot so lowest is in first position and highest in second position
    fun reorderValues() {
        if (values[0] > values[1]) {
            val aux = values[1]
            values[1] = values[0]
            values[0] = aux
        }
    }

    // Check if this is the bot that holds the expected pair of values
    fun checkValues(targetValues: List<Int>): Boolean = values == targetValues
}