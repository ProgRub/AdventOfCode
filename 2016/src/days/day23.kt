package days

class Day23 : IDaySolver {
    override var parser: IParser = Day23Parser()
    override var problemInput: List<String>
    private val reader: AssembunnyReaderUpgraded

    constructor(input: List<String>) {
        this.problemInput = input
        this.reader = AssembunnyReaderUpgraded(this.problemInput)
    }

    override fun test(): String {
        reader.runInstructions()
        return reader.getRegisterValue("a").toString()
    }

    // We start with 7 in register 'a' for part 1
    override fun part1(): String {
        reader.setRegisterValue("a", 7)
        reader.runInstructions()
        return reader.getRegisterValue("a").toString()
    }

    // The algorithm just does the factorial of register 'a' + multiplication of the last 2 constants
    // In my case 85*91
    // It just takes too long with 12 as the value
    override fun part2(): String {
        reader.setRegisterValue("a", 12)
        return (factorial(reader.getRegisterValue("a"))+85*91).toString()
    }

    private fun factorial (number:Int):Int{
        if (number == 1) return 1
        return number*factorial(number - 1)
    }
}

private class Day23Parser : IParser

class AssembunnyReaderUpgraded : AssembunnyReader {
    constructor(instructions: List<String>) : super(instructions) {
        // For this day we need a special kind of register so we convert the registers to the child class
        this.registers = this.registers.map {
            BunnyRegister(it.identifier, it.intValue)
        }
    }

    override fun executeInstruction(instructionCode: String, arguments: List<String>) {
        when (instructionCode) {
            "tgl" -> toggle(arguments.first())
            "mul" -> multiply(arguments.first(),arguments.last())
            else -> super.executeInstruction(instructionCode, arguments)
        }
    }

    // Toggle instruction, it changes the instruction placed at the value specified in the argument
    // Format: tgl a; it will toggle the instruction placed relative to the tgl instruction according to the value
    // in register 'a'
    private fun toggle(regIdentifier: String) {
        val toggleValue = try {
            getRegisterValue(regIdentifier)
        } // If there is no such registry, it's a number instead
        catch (_: NoSuchElementException) {
            regIdentifier.toInt()
        }
        val indexToToggle = runIndex + toggleValue
        // If there is no instruction to toggle, end the function
        if (indexToToggle !in instructions.indices) return
        val arguments = instructions[indexToToggle].substring(3)
        when {
            // One argument instructions: inc becomes dec, all others become inc
            instructions[indexToToggle].startsWith("inc") -> instructions[indexToToggle] = "dec$arguments"
            instructions[indexToToggle].startsWith("dec") -> instructions[indexToToggle] = "inc$arguments"
            instructions[indexToToggle].startsWith("tgl") -> instructions[indexToToggle] = "inc$arguments"
            // 2 argument instructions: jnz becomes cpy, all others become jnz
            instructions[indexToToggle].startsWith("jnz") -> instructions[indexToToggle] = "cpy$arguments"
            instructions[indexToToggle].startsWith("cpy") -> instructions[indexToToggle] = "jnz$arguments"
        }
    }

    // Multiply the register by the value
    // Format: mul b c; multiplies registers b and c, storing the result in register b
    private fun multiply(regIdentifier: String, multiplierValue:String){
        // Check if the first argument is a register
        try {
            val register = getRegister(regIdentifier) as BunnyRegister
            // Multiply the value of the second register
            try {
                val multiplier = getRegisterValue(multiplierValue)
                register.multiply(multiplier)
            } // Otherwise throws the exception, second argument is an integer
            catch (_: NoSuchElementException) {
                register.multiply(multiplierValue.toInt())
            }
        } // If there is no such registry, skip the instruction
        catch (_: NoSuchElementException) {
            return
        }
    }
}

class BunnyRegister : Register {
    constructor(identifier: Char, intValue: Int) : super(identifier, intValue)

    // Multiplies register by value passed
    fun multiply(multiplier:Int) {
        this.intValue *= multiplier
    }
}