import common


def part1():
    inputValues = common.getInputOfDay(9)
    grid = common.getGridFromLinesOfUninterruptedInts(inputValues)
    lowPoints = []
    for rowIndex in range(len(grid)):
        for columnIndex in range(len(grid[rowIndex])):
            checkingPoint = grid[rowIndex][columnIndex]
            adjacentPoints = []
            if rowIndex != 0:
                adjacentPoints += [grid[rowIndex - 1][columnIndex]]
            if rowIndex != len(grid) - 1:
                adjacentPoints += [grid[rowIndex + 1][columnIndex]]
            if columnIndex != 0:
                adjacentPoints += [grid[rowIndex][columnIndex - 1]]
            if columnIndex != len(grid[rowIndex]) - 1:
                adjacentPoints += [grid[rowIndex][columnIndex + 1]]
            if all([point > checkingPoint for point in adjacentPoints]):
                lowPoints += [checkingPoint]

    return sum([point + 1 for point in lowPoints])


def searchBasin(grid, rowIndex, columnIndex, visitedNodes, rowLength, columnLength):
    if rowIndex < 0 or columnIndex < 0 or rowIndex >= rowLength or columnIndex >= columnLength or grid[rowIndex][columnIndex] == 9 or (rowIndex, columnIndex) in visitedNodes:
        return
    visitedNodes.append((rowIndex, columnIndex))
    searchBasin(grid, rowIndex - 1, columnIndex, visitedNodes, rowLength, columnLength)
    searchBasin(grid, rowIndex + 1, columnIndex, visitedNodes, rowLength, columnLength)
    searchBasin(grid, rowIndex, columnIndex - 1, visitedNodes, rowLength, columnLength)
    searchBasin(grid, rowIndex, columnIndex + 1, visitedNodes, rowLength, columnLength)


def part2():
    inputValues = common.getInputOfDay(9)
    grid = common.getGridFromLinesOfUninterruptedInts(inputValues)
    basins = []
    lowPoints = []
    for rowIndex in range(len(grid)):
        for columnIndex in range(len(grid[rowIndex])):
            checkingPoint = grid[rowIndex][columnIndex]
            adjacentPoints = []
            if rowIndex != 0:
                adjacentPoints += [grid[rowIndex - 1][columnIndex]]
            if rowIndex != len(grid) - 1:
                adjacentPoints += [grid[rowIndex + 1][columnIndex]]
            if columnIndex != 0:
                adjacentPoints += [grid[rowIndex][columnIndex - 1]]
            if columnIndex != len(grid[rowIndex]) - 1:
                adjacentPoints += [grid[rowIndex][columnIndex + 1]]
            if all([point > checkingPoint for point in adjacentPoints]):
                lowPoints += [(rowIndex, columnIndex, checkingPoint)]

    for lowPoint in lowPoints:
        rowIndex, columnIndex, height = lowPoint
        basin = []
        searchBasin(grid, rowIndex, columnIndex, basin, len(grid[0]), len(grid))
        basins.append(len(basin))

    basins.sort(reverse=True)
    return basins[0] * basins[1] * basins[2]


def main():
    print(part2())


if __name__ == "__main__":
    main()
