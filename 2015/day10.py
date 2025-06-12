import sys

# setting path
sys.path.append("..\\AdventOfCode")

# importing
import common


# A function that generates the next 'Look-and-say' sequence based on the one received
# For more information check this https://en.wikipedia.org/wiki/Look-and-say_sequence
def lookAndSay(initial: str) -> str:
    newSequence = ""
    currentDigit = initial[0]
    count = 1
    for char in initial[1:]:
        if char != currentDigit:
            newSequence += str(count) + currentDigit
            currentDigit = char
            count = 1
        else:
            count += 1
    newSequence += str(count) + currentDigit
    return newSequence


# Performs the 'Look-and-say' algo the specified amount of times and returns the final sequence
def keepLookingAndSaying(initial: str, numTimes: int) -> str:
    finalSequence = initial
    for _ in range(numTimes):
        finalSequence = lookAndSay(finalSequence)
    return finalSequence


def part1():
    inputValues = common.getInput()
    return len(keepLookingAndSaying(inputValues[0], 40))


def part2():
    inputValues = common.getInput()
    return len(keepLookingAndSaying(inputValues[0], 50))


def main():
    print(part2())


if __name__ == "__main__":
    main()
