from common import common
import numpy as np

gridSize = 100
steps = 100
# Read Conway's Life to understand the values used for the rules (https://en.wikipedia.org/wiki/Conway's_Game_of_Life)
amountNeighboursOffTurnsOn = 3
amountNeighboursOnStaysOn = (2, 3)
offValue = 0
onValue = 1
offChar = "."
onChar = "#"


# This function parses the text of the input into a grid
def parseTextToGrid(text: list[str]) -> list[list[int]]:
    grid = []
    for line in text:
        grid += [[1 if char == onChar else 0 for char in line]]
    return grid


# This function determines the next state of the light in the given row and column depending on its' neighbours
def getNextLightState(row: int, col: int, lightGrid: list[list[int]]) -> int:
    state = lightGrid[row][col]
    # Part 2 variation, corner lights always stay on
    if (
        (row == col and row % (gridSize - 1) == 0)
        or (row == gridSize - 1 and col == 0)
        or (row == 0 and col == gridSize - 1)
    ):
        return 1
    rowAbove = max(0, row - 1)
    rowBelow = min(gridSize - 1, row + 1)
    leftColumn = max(0, col - 1)
    rightColumn = min(
        gridSize, col + 2
    )  # Add 2 instead of 1 because the indexing below excludes the end of the range
    neighbours = []
    # Only add to the list of neighbours if there is a row above, we're not in the first row
    if rowAbove != row:
        neighbours += [lightGrid[rowAbove][leftColumn:rightColumn]]
    neighbours += [lightGrid[row][leftColumn:rightColumn]]
    # Only add to the list of neighbours if there is a row below, we're not in the last row
    if rowBelow != row:
        neighbours += [lightGrid[rowBelow][leftColumn:rightColumn]]
    # We remove the current state from the sum because it's present in the neighbours array
    neighbourSum = np.sum(neighbours, dtype=int) - state
    # If it's on, check if it should be turned off
    # It gets turned off if the sum of its' neighbours isn't in the defined list of values
    if state == 1 and neighbourSum not in amountNeighboursOnStaysOn:
        state = 0
    # If it's off, check if it should be turned on
    # It gets turned on if the sum of its' neighbours equals the defined value
    elif state == 0 and neighbourSum == amountNeighboursOffTurnsOn:
        state = 1
    return state


# This function determines the next state of the light grid depending on the current state
def determineNextGrid(currentLightGrid: list[list[int]]) -> list[list[int]]:
    nextLightGrid = []
    for row in range(gridSize):
        gridRow = []
        for col in range(gridSize):
            gridRow += [getNextLightState(row, col, currentLightGrid)]
        nextLightGrid += [gridRow]
    return nextLightGrid


def part1():
    # The input in the file is not the input for part 1, it was modified for part 2, copy it from AdventOfCode
    inputValues = common.getInput()
    lightGrid = parseTextToGrid(inputValues.tolist())
    print(f"There are {np.sum(lightGrid,dtype=int)} lights turned on.")
    for step in range(steps):
        lightGrid = determineNextGrid(lightGrid.copy())
        print(
            f"Step {step + 1} completed; there are {np.sum(lightGrid,dtype=int)} lights turned on."
        )
    # We need to return the amount of lights turned on, simple sum does it
    return np.sum(lightGrid, dtype=int)


def part2():
    # The input is modified for part 2 from the one copied from AdventOfCode: corner lights are on
    inputValues = common.getInput()
    lightGrid = parseTextToGrid(inputValues.tolist())
    print(f"There are {np.sum(lightGrid,dtype=int)} lights turned on.")
    for step in range(steps):
        lightGrid = determineNextGrid(lightGrid.copy())
        print(
            f"Step {step + 1} completed; there are {np.sum(lightGrid,dtype=int)} lights turned on."
        )
    # We need to return the amount of lights turned on, simple sum does it
    return np.sum(lightGrid, dtype=int)


def main():
    print(part2())


if __name__ == "__main__":
    main()
