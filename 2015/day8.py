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


def encode(line: str) -> str:
    encodedLine = ""
    for char in line[1 : len(line) - 1]:
        match char:
            case "\\":
                encodedLine += "\\" + char
            case '"':
                encodedLine += '\\"'
            case _:
                encodedLine += char
    return f'"\\"{encodedLine}\\""'


def part2():
    inputValues = common.getInput()
    totalLength = 0
    stringsLength = 0
    for line in inputValues:
        totalLength += len(line)
        encodedLine = encode(line)
        print(f"{line}: {len(line)}")
        print(f"{encodedLine}: {len(encodedLine)}")
        stringsLength += len(encodedLine)
    return stringsLength - totalLength


def main():
    print(part2())


if __name__ == "__main__":
    main()
