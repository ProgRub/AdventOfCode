package days

class Day7 : IDaySolver {
    override var parser: IParser = Day7Parser()
    override var problemInput: List<String>

    // The input is a bunch of lines where each line is a string to check
    constructor(input: List<String>) {
        this.problemInput = input
    }

    override fun test(): String {
        return problemInput.count { supportsTLS(it) }.toString()
    }

    override fun part1(): String {
        return problemInput.count { supportsTLS(it) }.toString()
    }

    override fun part2(): String {
        return problemInput.count { supportsSSL(it) }.toString()
    }

    // Check if the "IP" supports "TLS", i.e. has at least one ABBA-string outside square brackets
    // and none inside square brackets
    private fun supportsTLS(ip: String): Boolean {
        var supportsTLS = false
        var inBrackets = false
        for (index in 0..<ip.length - 3) {
            when (ip[index]) {
                '[' -> inBrackets = true
                ']' -> inBrackets = false
                else -> {
                    val abbaCompatible = isAbbaCompatible(ip.substring(index, index + 4))
                    if (abbaCompatible && inBrackets) return false
                    supportsTLS = supportsTLS || abbaCompatible
                }
            }
        }
        return supportsTLS
    }

    // Check if the string passed is ABBA-compatible
    // String has 4 characters, first character equals the last one and 2nd == 3rd
    // String can't be all the same character
    private fun isAbbaCompatible(str: String): Boolean = when {
        str.length != 4 -> false
        else -> str[0] == str[3] && str[1] == str[2] && str[0] != str[1]
    }

    // Check if the string passed is ABA-compatible
    // String has 3 characters, first character equals the last one and 2nd character is differeny
    // String can't be all the same character
    private fun isAbaCompatible(str: String): Boolean = when {
        str.length != 3 -> false
        else -> str[0] == str[2] && str[0] != str[1]
    }

    // Check if the "IP" supports "SSL", i.e. has at least one ABA-string outside square brackets
    // and its reverse (BAB in this case) in square brackets
    private fun supportsSSL(ip: String): Boolean {
        var inBrackets = false
        var excerpt: String
        var babStringToFind: String
        val babStrings = mutableListOf<String>()
        for (index in 0..<ip.length - 2) {
            when (ip[index]) {
                '[' -> inBrackets = true
                ']' -> inBrackets = false
                else -> {
                    excerpt = ip.substring(index, index + 3)
                    if (isAbaCompatible(excerpt)) {
                        // If the string is ABA compatible and it has a valid corresponding BAB string then
                        // it's SSL compatible
                        if (hasBabMatchingString(excerpt, inBrackets, babStrings)) return true
                        // The end of the string is the negative of inBrackets to know if the string to find needs
                        // to be in or out of brackets: if we find the first in brackets then the reverse needs
                        // to be outside brackets and vice versa
                        babStringToFind = excerpt.elementAt(1).toString() +
                                excerpt.elementAt(0) +
                                excerpt.elementAt(1) + inBrackets
                        babStrings += babStringToFind
                    }
                }
            }
        }
        return false
    }

    private fun hasBabMatchingString(
        abaString: String,
        inBrackets: Boolean,
        babStrings: List<String>
    ): Boolean {
        // Filter the list to check only the strings that we're found in the reverse of in brackets
        // If we're currently in brackets we need to check the strings found outside brackets and vice versa
        val possibleStrings = babStrings.filter { it.substring(3) == (!inBrackets).toString() }
        // Check if the ABA string is a compatible with BAB strings found so far
        return possibleStrings.any { it.startsWith(abaString) }
    }
}

private class Day7Parser : IParser