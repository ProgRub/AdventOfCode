import common
import numpy as np

GRID_SIZE = 10
totalFlashes = 0

inputValues = common.getInputOfDay(11)
grid = np.array([list(map(int, r)) for r in inputValues])
def increaseEnergy(x, y, matrix):
    bottomBound = len(grid)
    rightBound = len(grid[0])
    if matrix[x][y] >= 10:
        matrix[x][y] = 0
        if x-1 >= 0: # UP
            if matrix[x-1][y] < 10 and matrix[x-1][y] != 0:
                matrix[x-1][y] += 1
        if y+1 < rightBound and x-1 >= 0: # UP-Right
            if matrix[x-1][y+1] < 10  and matrix[x-1][y+1] != 0:
                matrix[x-1][y+1] += 1
        if y+1 < rightBound: # RIGHT
            if matrix[x][y+1] < 10  and matrix[x][y+1] != 0:
                matrix[x][y+1] += 1
        if y+1 < rightBound and x+1 < bottomBound: # DOWN-Right
            if matrix[x+1][y+1] < 10 and matrix[x+1][y+1] != 0:
                matrix[x+1][y+1] += 1
        if x+1 < bottomBound: # DOWN
            if matrix[x+1][y] < 10 and matrix[x+1][y] != 0:
                matrix[x+1][y] += 1
        if y-1 >= 0 and x+1 < bottomBound: # DOWN-LEFT
            if matrix[x+1][y-1] < 10 and matrix[x+1][y-1] != 0:
                matrix[x+1][y-1] += 1
        if y-1 >= 0: # LEFT
            if matrix[x][y-1] < 10 and matrix[x][y-1] != 0:
                matrix[x][y-1] += 1
        if x-1 >= 0 and y-1 >= 0: # UP-LEFT
            if matrix[x-1][y-1] < 10 and matrix[x-1][y-1] != 0:
                matrix[x-1][y-1] += 1
    return matrix

def updateSurroundingCellsAfterFlash(grid, rowIndex, columnIndex):
    global totalFlashes
    grid[rowIndex][columnIndex] = 0
    totalFlashes += 1
    canGoUp = rowIndex > 0
    canGoDown = rowIndex + 1 < GRID_SIZE
    canGoLeft = columnIndex > 0
    canGoRight = columnIndex + 1 < GRID_SIZE
    if canGoUp:
        if grid[rowIndex - 1][columnIndex] != 0: grid[rowIndex - 1][columnIndex] += 1
        if grid[rowIndex - 1][columnIndex] > 9: updateSurroundingCellsAfterFlash(grid, rowIndex - 1, columnIndex)
        if canGoLeft:
            if grid[rowIndex - 1][columnIndex - 1] != 0: grid[rowIndex - 1][columnIndex - 1] += 1
            if grid[rowIndex - 1][columnIndex - 1] > 9: updateSurroundingCellsAfterFlash(grid, rowIndex - 1, columnIndex - 1)
        if canGoRight:
            if grid[rowIndex - 1][columnIndex + 1] != 0: grid[rowIndex - 1][columnIndex + 1] += 1
            if grid[rowIndex - 1][columnIndex + 1] > 9: updateSurroundingCellsAfterFlash(grid, rowIndex - 1, columnIndex + 1)
    if canGoDown:
        if grid[rowIndex + 1][columnIndex] != 0: grid[rowIndex + 1][columnIndex] += 1
        if grid[rowIndex + 1][columnIndex] > 9: updateSurroundingCellsAfterFlash(grid, rowIndex + 1, columnIndex)
        if canGoLeft:
            if grid[rowIndex + 1][columnIndex - 1] != 0: grid[rowIndex + 1][columnIndex - 1] += 1
            if grid[rowIndex + 1][columnIndex - 1] > 9: updateSurroundingCellsAfterFlash(grid, rowIndex + 1, columnIndex - 1)
        if canGoRight:
            if grid[rowIndex + 1][columnIndex + 1] != 0: grid[rowIndex + 1][columnIndex + 1] += 1
            if grid[rowIndex + 1][columnIndex + 1] > 9: updateSurroundingCellsAfterFlash(grid, rowIndex + 1, columnIndex + 1)

    if canGoLeft:
        if grid[rowIndex][columnIndex - 1] != 0: grid[rowIndex][columnIndex - 1] += 1
        if grid[rowIndex][columnIndex - 1] > 9: updateSurroundingCellsAfterFlash(grid, rowIndex, columnIndex - 1)
    if canGoRight:
        if grid[rowIndex][columnIndex + 1] != 0: grid[rowIndex][columnIndex + 1] += 1
        if grid[rowIndex][columnIndex + 1] > 9: updateSurroundingCellsAfterFlash(grid, rowIndex, columnIndex + 1)


def part1():
    inputValues = common.getInputOfDay(11)
    grid = common.getGridFromLinesOfUninterruptedInts(inputValues)
    STEPS = 100
    for step in range(STEPS):
        gridAtStartOfStep=grid.copy()
        for rowIndex in range(GRID_SIZE):
            for columnIndex in range(GRID_SIZE):
                if gridAtStartOfStep[rowIndex][columnIndex]==0 or grid[rowIndex][columnIndex]!=0:
                    grid[rowIndex][columnIndex] += 1
                if grid[rowIndex][columnIndex] > 9:
                    updateSurroundingCellsAfterFlash(grid, rowIndex, columnIndex)

    return totalFlashes


def part2():
    inputValues = common.getInputOfDay(11)
    return 0


def main():
    global grid
    print(part1())
    totalOctopuses = len(grid) * len(grid[0])
    flashes = 0
    stepFlashes = 0
    step = 0
    while True:
        grid += 1
        for row in range(len(grid)):
            for column in range(len(grid[0])):
                grid = increaseEnergy(row, column, grid)
        tens = [(r, c) for r in range(len(grid)) for c in range(len(grid[r])) if grid[r][c] == 10]
        while tens:
            for ten in tens:
                grid = increaseEnergy(ten[0], ten[1], grid)
            tens = [(r, c) for r in range(len(grid)) for c in range(len(grid[r])) if grid[r][c] == 10]

        stepFlashes = len([(r, c) for r in range(len(grid)) for c in range(len(grid[r])) if grid[r][c] == 0])
        step += 1
        if step < 100:
            flashes += stepFlashes
        if stepFlashes == totalOctopuses:
            break
    print('Flashes:', flashes)


if __name__ == "__main__":
    main()
