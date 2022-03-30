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
    return inputValues


def main():
    print(part1())


if __name__ == "__main__":
    main()
