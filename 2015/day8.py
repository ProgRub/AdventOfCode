import sys

# setting path
sys.path.append("..\\AdventOfCode")

# importing
import common
import codecs


def part1():
    inputValues = common.getInput()
    totalLength = 0
    stringsLength = 0
    for line in inputValues:
        totalLength += len(line)
        line = line[1 : len(line) - 1]
        line = bytes(line, encoding="ascii")
        line = codecs.decode(line, encoding="unicode_escape")
        stringsLength += len(line)
    return totalLength - stringsLength


def part2():
    inputValues = common.getInput()
    return inputValues


def main():
    print(part1())


if __name__ == "__main__":
    main()
