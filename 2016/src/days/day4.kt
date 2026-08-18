package days

class Day4 : IDaySolver {
    override var parser: IParser = Day4Parser()
    override var problemInput: List<String>
    private var roomCodes: List<RoomCode>

    // Input consists of lines in the format
    // aaaaa-bbbb-cc-eeeeee-...-123[abcde]
    // Where 123 is the room id, in between squared brackets we have the checksum
    // and the rest before is the room name, where each dash is a space
    constructor(input: List<String>) {
        this.problemInput = input
        this.roomCodes = this.problemInput.map { RoomCode(it) }
    }

    override fun test(): String {
        val sum = roomCodes.sumOf { roomCode -> if (roomCode.isReal()) roomCode.id else 0 }
        return sum.toString()
    }

    override fun part1(): String {
        val sum = roomCodes.sumOf { roomCode -> if (roomCode.isReal()) roomCode.id else 0 }
        return sum.toString()
    }

    // We have to find the room ID where north pole objects are stored, that's why we filter the real names for "north"
    override fun part2(): String {
        val realRooms = roomCodes.filter { roomCode -> roomCode.isReal() }
        val northPoleStorage = realRooms.first { it.findRealName().contains("north") }
        return northPoleStorage.id.toString()
    }
}

internal class RoomCode {
    var checkSum: String
    var id: Int = -1
    var name: String
    var nameSplit: List<String>

    constructor(line: String) {
        val splitIterator = line.splitToSequence("-")
        name = ""
        checkSum = ""
        nameSplit = listOf()
        for (value in splitIterator) {
            // If it's the last value of the iterator it's the id and the checksum; ex.: 567[abcde]
            if (value == splitIterator.last()) {
                id = value.substring(0, 3).toInt()
                checkSum = value.substring(4, 9)
            }
            // Otherwise add to the name, and the split list of the name
            else {
                name += value
                nameSplit += value
            }
        }
    }

    // Determines if the encrypted name of the room  points to a real room
    // It's real if the characters in the checksum are the most common characters in the name
    // Ordered in the checksum from most common to least common, in the case of a tie needs to be ordered alphabetically
    internal fun isReal(): Boolean {
        val lettersCount = sortedMapOf<Char, Int>()
        this.name.toCharArray().forEach { letter -> lettersCount[letter] = this.name.count { it == letter } }
        var previousHigh = lettersCount.values.maxOrNull() ?: 0
        var previousLetter = this.checkSum[0]
        // If the first letter of the checksum doesn't appear in the word
        // or the count of the first letter doesn't equal the biggest value then checksum isn't ordered
        if (previousLetter !in lettersCount) return false
        if (lettersCount[previousLetter]!! != previousHigh) return false
        // Start from the second letter
        for (letter in this.checkSum.substring(1)) {
            // If the checksum letter doesn't exist in the name, can't be in the checksum
            if (letter !in lettersCount) return false
            val count = lettersCount[letter]!!
            // If the count of this letter is greater than the count of the previous one then checksum isn't ordered
            if (count > previousHigh) return false
            // If the count of this letter is equal to the previous one and
            // this letter is "smaller" than the previous one then the checksum isn't ordered alphabetically in a tie
            if (count == previousHigh && letter < previousLetter) return false
            previousHigh = count
            previousLetter = letter
        }
        return true
    }

    // Decrypts the name, to decrypt it we need to shift the letters forward the amount of times specified by the id
    internal fun findRealName(): String {
        var realName = ""
        val lastLetterValue = 'z'.code
        val firstLetterCode = 'a'.code -1 // Remove one so we consider a
        val divider = lastLetterValue - firstLetterCode // number of letters in alphabet
        val shiftForward = this.id % divider
        for (encrypWord in this.nameSplit) {
            for (letter in encrypWord) {
                // Find the letter we get by shifting forward the calculated amount of times
                var code = letter.code + shiftForward
                // If we've gone past z need to come back around
                if (code > lastLetterValue) code = code - lastLetterValue + firstLetterCode
                realName += Char(code).toString()
            }
            realName += " "
        }
        return realName
    }
}

class Day4Parser : IParser