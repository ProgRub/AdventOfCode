package days

import java.security.MessageDigest

class Day5 : IDaySolver {
    override var parser: IParser = Day5Parser()
    override var problemInput: List<String>
    private val passwordLength = 8
    private val startingZerosLength = 5

    // Input data is just one line representing the door ID
    constructor(input: List<String>) {
        this.problemInput = input
    }

    override fun test(): String {
        return findPassword()
    }

    override fun part1(): String {
        return findPassword()
    }

    override fun part2(): String {
        return findHarderPassword()
    }

    /*
    The eight-character password for the door is generated one character at a time
    by finding the MD5 hash of some Door ID (your puzzle input) and an increasing integer index (starting with 0).
    A hash indicates the next character in the password if its hexadecimal representation starts with five zeroes.
    If it does, the sixth character in the hash (first after the zeroes) is the next character of the password.
    */
    private fun findPassword(): String {
        val md = MessageDigest.getInstance("MD5")
        val roomId = this.problemInput[0]
        val zeroSequence = "0".repeat(this.startingZerosLength)
        var password = ""
        var index = 0
        var hashTarget: String
        var hashedValue: String
        // While we haven't hit the specified password length
        while (password.length < passwordLength) {
            do {
                hashTarget = roomId + index // the string to hash is the room id concatenated with the index
                hashedValue = md.digest(hashTarget.toByteArray()).toHexString()
                index++
            } while (!hashedValue.startsWith(zeroSequence))
            // add the first character after the zeroes, ex.: first character after 5 zeroes is at index 5
            password += hashedValue[this.startingZerosLength]
        }
        return password
    }

    /*
    The eight-character password for the door is generated one character at a time
    by finding the MD5 hash of some Door ID (your puzzle input) and an increasing integer index (starting with 0).
    A hash indicates the next character in the password if its hexadecimal representation starts with five zeroes.
    If it does, the sixth character in the hash indicates the position in the password of the seventh character
    We only care about the first result, if it's already occupied we skip, also ignore invalid positions
    */
    private fun findHarderPassword(): String {
        val md = MessageDigest.getInstance("MD5")
        val roomId = this.problemInput[0]
        val zeroSequence = "0".repeat(this.startingZerosLength)
        val password = " ".repeat(passwordLength).toCharArray()
        var index = 0
        var hashTarget: String
        var hashedValue: String
        var passwordPosition: Int
        val zeroCharCode = '0'.code
        // While we still haven't filled the whole password
        while (' ' in password) {
            do {
                hashTarget = roomId + index // the string to hash is the room id concatenated with the index
                hashedValue = md.digest(hashTarget.toByteArray()).toHexString()
                index++
            } while (!hashedValue.startsWith(zeroSequence))
            passwordPosition = hashedValue[this.startingZerosLength].code - zeroCharCode
            // If the position is already filled or it's invalid
            if (passwordPosition >= this.passwordLength || password[passwordPosition] != ' ') continue
            // add the character after the password position to the specified position in the password
            password[passwordPosition] = hashedValue[this.startingZerosLength + 1]
        }
        return password.joinToString("") // convert the char array to string
    }
}

private class Day5Parser : IParser