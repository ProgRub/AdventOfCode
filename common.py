import inspect
import os.path
import sys


def getInput():
    filename = os.path.basename(inspect.getsourcefile(sys._getframe(1)))
    day = filename[3:filename.index('.')]
    with open("inputs\day{}.txt".format(day)) as f:
        return [line.strip() for line in f if line.strip()]


def getLineOfNumbersAsInts(line, separator):
    return [int(number) for number in line.split(separator)]


def getGridFromLinesOfUninterruptedInts(lines):
    grid = []
    for line in lines:
        row = []
        for character in line:
            row += [int(character)]
        grid += [row]
    return grid
