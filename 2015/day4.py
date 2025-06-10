import hashlib

import sys

# setting path
sys.path.append("..\\AdventOfCode")

# importing
import common

def part1():
    inputValues = common.getInput()
    return getLowestHashNumber(inputValues, 5)


def getLowestHashNumber(inputValues, zeroes):
    secretCode = inputValues[0]
    number = 1
    while not hashlib.md5((secretCode + str(number)).encode()).hexdigest()[:zeroes] == "000000000000000000000"[:zeroes]:
        number += 1
    return number


def part2():
    inputValues = common.getInput()
    return getLowestHashNumber(inputValues, 6)


def main():
    print(part2())


if __name__ == "__main__":
    main()
