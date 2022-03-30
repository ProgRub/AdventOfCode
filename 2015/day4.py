import common


def part1():
    inputValues = common.getInputOfDay()
    directions = inputValues[0]
    indexX = 0
    indexY = 0
    visitedSpots = set()
    for move in directions:
        visitedSpots.add((indexX, indexY))
        if move == '>':
            indexX += 1
            continue
        if move == '<':
            indexX -= 1
            continue
        if move == '^':
            indexY += 1
            continue
        if move == 'v':
            indexY -= 1
            continue
    return len(visitedSpots)


def part2():
    inputValues = common.getInputOfDay()
    directions = inputValues[0]
    indexXRealSanta = 0
    indexYRealSanta = 0
    indexXRobotSanta = 0
    indexYRobotSanta = 0
    indexX = 0
    indexY = 0
    visitedSpots = set()
    moveCounter = 1
    for move in directions:
        if moveCounter % 2 == 0:
            indexX = indexXRobotSanta
            indexY = indexYRobotSanta
        else:
            indexX = indexXRealSanta
            indexY = indexYRealSanta
        visitedSpots.add((indexX, indexY))
        if move == '>':
            indexX += 1
        elif move == '<':
            indexX -= 1
        elif move == '^':
            indexY += 1
        elif move == 'v':
            indexY -= 1
        if moveCounter % 2 == 0:
            indexXRobotSanta = indexX
            indexYRobotSanta = indexY
        else:
            indexXRealSanta = indexX
            indexYRealSanta = indexY
        moveCounter += 1
    return len(visitedSpots)


def main():
    print(part2())


if __name__ == "__main__":
    main()
