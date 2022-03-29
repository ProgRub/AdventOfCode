import common


def part1():
    inputValues = common.getInputOfDay(3)
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
    inputValues = common.getInputOfDay(2)
    totalRibbon = 0
    for box in inputValues:
        measures = box.split('x')
        length = int(measures[0])
        width = int(measures[1])
        height = int(measures[2])
        measuresAsInts = [length, width, height]
        measuresAsInts.sort()
        bow = length * width * height
        ribbonToWrap = measuresAsInts[0] * 2 + measuresAsInts[1] * 2
        totalRibbon += ribbonToWrap + bow
    return totalRibbon


def main():
    print(part1())


if __name__ == "__main__":
    main()
