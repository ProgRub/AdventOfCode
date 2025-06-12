import sys

# setting path
sys.path.append("..\\AdventOfCode")

# importing
import common


def part1():
    inputValues = common.getInput()
    counter = 0
    for parenthesis in inputValues[0]:
        if parenthesis == "(":
            counter += 1
        else:
            counter -= 1
    return counter


def part2():
    inputValues = common.getInput()
    counter = 0
    for index, parenthesis in enumerate(inputValues[0]):
        parenthesis = inputValues[0][index]
        if parenthesis == "(":
            counter += 1
        else:
            counter -= 1
            if counter < 0:
                return index + 1
    return counter


def main():
    print(part2())


if __name__ == "__main__":
    main()
