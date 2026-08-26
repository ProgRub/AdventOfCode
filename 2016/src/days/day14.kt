package days

import java.security.MessageDigest

class Day14 : IDaySolver {
    override var parser: IParser = Day14Parser()
    override var problemInput: List<String>
    private val targetKey = 64
    private val tripletKeyRange = 1000
    private val keyStretchingAmount = 2016

    // Problem input is just one line with the salt value (cryptography)
    constructor(input: List<String>) {
        this.problemInput = input
    }

    override fun test(): String {
        return findNKeyIndex(targetKey, keyStretchingAmount).toString()
    }

    // Part 1 is to check without additional hashings
    override fun part1(): String {
        return findNKeyIndex(targetKey).toString()
    }

    // Part 2 is to check with the specified amount of additional hashings
    override fun part2(): String {
        return findNKeyIndex(targetKey, keyStretchingAmount).toString()
    }

    private fun findNKeyIndex(targetNKey: Int, additionalHashings: Int = 0): Int {
        val md = MessageDigest.getInstance("MD5")
        val salt = this.problemInput[0]
        var keysFound = 0
        var index = 0
        var hashTarget: String
        var hashedValue = ""
        // Since we don't find the keys in sequence, upon finding the Nth key we define what is the max index
        // to find an earlier key that would be the Nth key
        var earliestKeyCutoff = Int.MAX_VALUE
        // Map where the key indicates the max index possible to find a key for the triplet (value) found
        val tripletsToCheck = mutableMapOf<Int, String>()
        val tripletsSet = mutableSetOf<String>()
        // Since we need to check forward 1000 hashes when we find a triplet in a hash it would be wasteful
        // to regenerate those hashes again so we check if the hash is key to any of the triplets found before,
        // saving the indexes of the hashes it's key to
        val indexesKeys = sortedSetOf<Int>()
        while (keysFound < targetNKey || index < earliestKeyCutoff) {
            var stretchingCounter = 0
            hashTarget = salt + index
            // If key stretching is asked n amount of times, we only want the n hash
            // First we find the hash for salt and the index, then we find the hash of that hash, and so on
            // Until we've done this the specified amount of times
            while (stretchingCounter < additionalHashings + 1) {
                hashedValue = md.digest(hashTarget.toByteArray()).toHexString()
                stretchingCounter++
                hashTarget = hashedValue
            }
            // If we've hit the max number of hashes to check for a triplet, remove it from the map
            if (tripletsToCheck.containsKey(index)) {
                tripletsToCheck.remove(index)
            }
            val triplet = findFirstTriplet(hashedValue)
            // If there is a triplet in the hash, add it to the list to check along with the max index to check
            if (triplet != "") {
                tripletsToCheck[index + 1 + tripletKeyRange] = triplet
                tripletsSet.add(triplet)
            }
            // Check if this hash is key to any of the triplets found, returning the triplet it's key to
            val tripletFound = findTripletOfKey(hashedValue, tripletsSet)
            if (tripletFound != "") {
                tripletsSet.remove(tripletFound)
                // Need to go through the map and find all previous hashes that have this hash complete them as a key
                for ((maxIndex, triplet) in tripletsToCheck) {
                    val originalIndex = maxIndex - 1 - tripletKeyRange
                    // If the current hash has a triplet and a 5 char sequence, it cannot be considered a key of itself
                    if (triplet == tripletFound && originalIndex != index) {
                        keysFound++
                        indexesKeys.add(originalIndex)
                        // Since we can't remove mid for loop, we set the triplet to an impossible sequence so it
                        // doesn't hit again in the future (the hash is in hexadecimal)
                        tripletsToCheck[maxIndex] = "///"
                        // If we've reached the desired number of keys, we set the cutoff for earliest possible Nth key
                        // Although we've hit the target, we need to find the target nth key, in sequence
                        // So we set the cutoff at the target index of the triplet found because we only
                        // care about keys before this
                        if (keysFound == targetNKey) earliestKeyCutoff = maxIndex
                    }
                }
            }
            index += 1
        }
        // The array of indexes of keys is already sorted
        return indexesKeys.elementAt(targetNKey - 1)
    }

    // Finds the first sequence of three same characters (ex. 'aaa') aka triplet. Returns blank if there is none
    private fun findFirstTriplet(hashHexadecimal: String): String {
        for (index in 0..<hashHexadecimal.length - 2) {
            if (hashHexadecimal[index] == hashHexadecimal[index + 1] &&
                hashHexadecimal[index + 1] == hashHexadecimal[index + 2]
            )
                return hashHexadecimal[index].toString().repeat(3)
        }
        return ""
    }

    // Check if the hash represents a key i.e. it has a sequence of 5 characters in a row with the same characters
    // from a previous triplet
    private fun findTripletOfKey(hashHexadecimal: String, triplets: Set<String>): String {
        for (index in 0..<hashHexadecimal.length - 4) {
            // If there is a sequence of 5 characters in a row, check if relates to a triplet
            if (hashHexadecimal[index] == hashHexadecimal[index + 1] &&
                hashHexadecimal[index + 1] == hashHexadecimal[index + 2] &&
                hashHexadecimal[index + 2] == hashHexadecimal[index + 3] &&
                hashHexadecimal[index + 3] == hashHexadecimal[index + 4]
            ) {
                // Only stop the loop if the sequence is of the same character as one of the triplets
                // And return the triplet because this one hash might be key to multiple different
                // previous hashes that had the same triplet
                if (triplets.any { it[0] == hashHexadecimal[index] })
                    return hashHexadecimal[index].toString().repeat(3)
            }
        }
        return ""
    }
}

class Day14Parser : IParser