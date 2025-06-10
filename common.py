import inspect
import os.path
import sys
import numpy as np


def getInput():
    directory = os.path.dirname(inspect.getsourcefile(sys._getframe(1)))
    filename = os.path.basename(inspect.getsourcefile(sys._getframe(1)))
    day = filename[3:filename.index('.')]
    with open(os.path.join(directory, "inputs", "day{}.txt").format(day)) as f:
        return np.array([line.strip() for line in f if line.strip()])


def getLineOfNumbersAsInts(line, separator):
    return np.array([int(number) for number in line.split(separator)])


def getGridFromLinesOfUninterruptedInts(lines):
    grid = np.empty((len(lines), len(lines[0])))
    index = 0
    for line in lines:
        grid[index] = np.array([int(character) for character in line])
        index += 1
    return grid
