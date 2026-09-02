package days

class Day12 : IDaySolver {
    override var parser: IParser = Day12Parser()
    override var problemInput: List<String>
    private val reader: AssembunnyReader

    // The input is a list of lines where each line is an 'assembly' instruction
    constructor(input: List<String>) {
        this.problemInput = input
        this.reader = AssembunnyReader(this.problemInput)
    }

    override fun test(): String {
        reader.runInstructions()
        return reader.getRegisterValue("a").toString()
    }

    // We want the value of the 'a' register after the instructions are run
    override fun part1(): String {
        reader.runInstructions()
        return reader.getRegisterValue("a").toString()
    }

    // Change from part 2 is register 'c' starts with 1
    override fun part2(): String {
        reader.setRegisterValue("c", 1)
        reader.runInstructions()
        return reader.getRegisterValue("a").toString()
    }
}

private class Day12Parser : IParser

open class Register {
    val identifier: Char
    var intValue: Int

    constructor(identifier: Char, intValue: Int) {
        this.identifier = identifier
        this.intValue = intValue
    }

    fun copy(newValue: Int) {
        this.intValue = newValue
    }

    fun increase() {
        this.intValue++
    }

    fun decrease() {
        this.intValue--
    }

    // The jump can only happen if the register's value isn't zero
    fun jump(amountToJump: Int): Int {
        val multiplier = if (intValue == 0) 0 else 1
        return multiplier * amountToJump
    }
}

open class AssembunnyReader {
    protected var registers: List<Register>
    protected val instructions: MutableList<String>
    protected var runIndex: Int

    constructor(instructions: List<String>) {
        this.registers = mutableListOf(
            Register('a', 0),
            Register('b', 0),
            Register('c', 0),
            Register('d', 0)
        )
        this.instructions = instructions.toMutableList()
        this.runIndex = 0
    }

    // Runs the instructions passed to the Assembunny reader
    fun runInstructions() {
        runIndex = 0
        // Has to be a while because there are jump instructions that may change the index of instruction to run
        while (runIndex < instructions.size) {
            val split = instructions[runIndex].split(" ")
            executeInstruction(split[0], split.subList(1, split.size))
            runIndex++
        }
    }

    // Executes the instruction by its code, passing its arguments
    open fun executeInstruction(instructionCode: String, arguments: List<String>) {
        when (instructionCode) {
            "cpy" -> copy(arguments.last(), arguments.first())
            "inc" -> increase(arguments.last())
            "dec" -> decrease(arguments.last())
            "jnz" -> jump(arguments.first(), arguments.last())
        }
    }

    // Copies the value passed to the register specified. The value passed might be another register or an Int
    // Format: cpy 41 a; copies 41 into register 'a'. First argument may be another register
    private fun copy(regIdentifier: String, copyValue: String) {
        try {
            val register = getRegister(regIdentifier)
            // If the first argument of the instruction (value to copy) is a register
            try {
                val baseRegister = getRegister(copyValue)
                register.copy(baseRegister.intValue)
            } // Otherwise throws the exception, first argument is an integer
            catch (_: NoSuchElementException) {
                register.copy(copyValue.toInt())
            }
        } // If there is no such registry, skip the instruction
        catch (_: NoSuchElementException) {
            return
        }
    }

    // Increases the register specified
    // Format: inc b; increments register 'b' value by 1
    private fun increase(regIdentifier: String) {
        try {
            val register = getRegister(regIdentifier)
            register.increase()
        } // If there is no such registry, skip the instruction
        catch (_: NoSuchElementException) {
            return
        }
    }

    // Decreases the register specified
    // Format: dec b; decrements register 'b' value by 1
    private fun decrease(regIdentifier: String) {
        try {
            val register = getRegister(regIdentifier)
            register.decrease()
        } // If there is no such registry, skip the instruction
        catch (_: NoSuchElementException) {
            return
        }
    }

    // Jump instruction, jump only occurs if the value of the first argument, number or register, is not zero
    // Format: jnz c 2; the 2 instruction jump only occurs if register 'c' has a value != 0
    // Takes out 1 from the amount to jump because index is always incremented
    private fun jump(regIdentifier: String, jumpValue: String) {
        // If the second argument of the instruction (the amount to jump) is a register
        val amountToJump = try {
            getRegister(jumpValue).intValue - 1
        }  // Otherwise, 2nd argument is a number, we only jump if this number != 0
        catch (_: NoSuchElementException) {
            jumpValue.toInt() - 1
        }
        // If the first argument of the instruction (value that indicates if we can jump) is a register
        try {
            runIndex += getRegister(regIdentifier).jump(amountToJump)
        }  // Otherwise, 2nd argument is a number, we only jump if this number != 0
        catch (_: NoSuchElementException) {
            if (regIdentifier.toInt() != 0) runIndex += amountToJump
        }
    }

    // SGets the value for the register specified. If register doesn't exist throws NoSuchElementException
    fun getRegisterValue(registerIdentifier: String): Int = getRegister(registerIdentifier).intValue

    // Sets the value for the register specified. If register doesn't exist throws NoSuchElementException
    fun setRegisterValue(registerIdentifier: String, newValue: Int) {
        getRegister(registerIdentifier).intValue = newValue
    }

    // Returns the register asked for.
    // Throws NoSuchElementException if it doesn't exist
    protected fun getRegister(regIdentifier: String): Register =
        this.registers.first { it.identifier == regIdentifier.first() }
}