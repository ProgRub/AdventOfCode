package days

class Day12 : IDaySolver {
    override var parser: IParser = Day12Parser()
    override var problemInput: List<String>
    private val registers: MutableList<Register>

    // The input is a list of lines where each line is an 'assembly' instruction
    constructor(input: List<String>) {
        this.problemInput = input
        this.registers = mutableListOf(
            Register('a', 0),
            Register('b', 0),
            Register('c', 0),
            Register('d', 0)
        )

    }

    override fun test(): String {
        runInstructions()
        return registers.first { it.identifier == 'a' }.intValue.toString()
    }

    // We want the value of the 'a' register after the instructions are run
    override fun part1(): String {
        runInstructions()
        return registers.first { it.identifier == 'a' }.intValue.toString()
    }

    // Change from part 2 is register 'c' starts with 1
    override fun part2(): String {
        this.registers.first { it.identifier == 'c' }.intValue = 1
        runInstructions()
        return registers.first { it.identifier == 'a' }.intValue.toString()
    }

    private fun runInstructions() {
        var index = 0
        // Has to be a while because there are jump instructions that may change the index of instruction to run
        while (index < this.problemInput.size) {
            val instruction = this.problemInput[index]
            index = executeInstruction(instruction, index)
        }
    }

    private fun executeInstruction(instruction: String, currentIndex: Int): Int {
        val split = instruction.split(" ")
        var newIndex = currentIndex + 1
        lateinit var register: Register
        when (split[0]) {
            // Copy instruction, format: cpy 41 a; copies 41 into register a
            "cpy" -> {
                register = this.registers.first { it.identifier == split.last()[0] }
                // If the first argument of the instruction is a register
                if (this.registers.any { it.identifier == split[1][0] }) {
                    val baseRegister = this.registers.first { it.identifier == split[1][0] }
                    register.copy(baseRegister.intValue)
                } else {
                    register.copy(split[1].toInt())
                }
            }
            // Increment instruction, format: inc b; increments register b's value by 1
            "inc" -> {
                register = this.registers.first { it.identifier == split.last()[0] }
                register.increase()
            }
            // Decrement instruction, format: dec b; decrements register b's value by 1
            "dec" -> {
                register = this.registers.first { it.identifier == split.last()[0] }
                register.decrease()
            }
            // Jump instruction, format: jnz c 2; if the value of register a is different than zero
            // Then it would jump past the next instruction, incrementing the index by 2
            "jnz" -> {
                // Take out 1 from the amount to jump because index is already incremented
                val amountToJump = split.last().toInt() - 1
                // If the first argument of the instruction is a register
                if (this.registers.any { it.identifier == split[1][0] }) {
                    register = this.registers.first { it.identifier == split[1][0] }
                    newIndex += register.jump(amountToJump)
                } else { // Otherwise, 2nd argument is a number, we only jump if this number != 0
                    if (split[1].toInt() != 0) newIndex += amountToJump
                }
            }
        }
        return newIndex
    }
}

private class Day12Parser : IParser

private class Register {
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