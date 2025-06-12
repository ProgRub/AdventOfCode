import sys

# setting path
sys.path.append("..\\AdventOfCode")

# importing
import common


def part1():
    inputValues = common.getInput()
    niceStrings = 0
    for string in inputValues:
        amountOfVowels = 0
        twoLettersInARow = False
        if "ab" in string or "cd" in string or "pq" in string or "xy" in string:
            continue
        for index, char in enumerate(string):
            if char == "a" or char == "e" or char == "i" or char == "o" or char == "u":
                amountOfVowels += 1
            if index < len(string) - 1 and string[index + 1] == char:
                twoLettersInARow = True
        if amountOfVowels >= 3 and twoLettersInARow:
            niceStrings += 1

    return niceStrings


def part2():
    inputValues = common.getInput()
    niceStrings = 0
    for string in inputValues:
        pairs = []
        for index, char in enumerate(string[: len(string) - 1]):
            pairs += [char + string[index + 1]]
        pairOfTwoLetters = False
        letterSandwich = False
        for pairIndex, pair in enumerate(pairs[: len(pairs) - 1]):
            if pair[0] == pair[1] and (
                pair[0] == pairs[pairIndex + 1][1]
                or (pairIndex > 0 and pair[0] == pairs[pairIndex - 1][0])
            ):
                pairOfTwoLetters = False
            if pair in pairs[pairIndex + 1 :]:
                pairOfTwoLetters = True
            if pair == pairs[pairIndex + 1][::-1]:
                letterSandwich += True
        if pairOfTwoLetters and letterSandwich:
            niceStrings += 1
    return niceStrings


def main():
    print(part2())


if __name__ == "__main__":
    main()
