import common


def part1():
    inputValues = common.getInput()
    niceStrings = 0
    for string in inputValues:
        amountOfVowels = 0
        twoLettersInARow = False
        if "ab" in string or "cd" in string or "pq" in string or "xy" in string:
            continue
        for index in range(len(string)):
            if string[index] == 'a' or string[index] == 'e' or string[index] == 'i' or string[index] == 'o' or string[index] == 'u':
                amountOfVowels += 1
            if index < len(string) - 1 and string[index + 1] == string[index]:
                twoLettersInARow = True
        if amountOfVowels >= 3 and twoLettersInARow:
            niceStrings += 1

    return niceStrings


def part2():
    inputValues = common.getInput()
    niceStrings = 0
    for string in inputValues:
    # for string in ["qjhvhtzxzqqjkmpb","xxyxx","uurcxstgmygtbstg","ieodomkazucvgmuy"]:
        pairs = []
        for index in range(len(string) - 1):
            pairs += [string[index] + string[index + 1]]
        pairOfTwoLetters = False
        letterSandwich = False
        for pairIndex in range(len(pairs) - 1):
            if pairs[pairIndex][0] == pairs[pairIndex][1] and (pairs[pairIndex][0] == pairs[pairIndex + 1][1] or (pairIndex>0 and pairs[pairIndex][0] == pairs[pairIndex - 1][0])):
                pairOfTwoLetters = False
            if pairs[pairIndex] in pairs[pairIndex + 1:]:
                pairOfTwoLetters = True
            if pairs[pairIndex] == pairs[pairIndex + 1][::-1]:
                letterSandwich += True
        if pairOfTwoLetters and letterSandwich:
            niceStrings += 1
    return niceStrings


def main():
    print(part2())


if __name__ == "__main__":
    main()
