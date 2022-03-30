import common
from collections import Counter

MAX_VALUE = 999


def part1():
    inputValues = common.getInput()
    lineSegments = [line.split(" -> ") for line in inputValues]
    lineSegmentsAsPoints = [
        [common.getLineOfNumbersAsInts(lineSegment[0], ','), common.getLineOfNumbersAsInts(lineSegment[1], ',')] for
        lineSegment in lineSegments]
    index = 0
    while index < len(lineSegmentsAsPoints):
        if lineSegmentsAsPoints[index][0][0] == lineSegmentsAsPoints[index][1][0] or lineSegmentsAsPoints[index][0][1] == lineSegmentsAsPoints[index][1][1]:
            index += 1
        else:
            lineSegmentsAsPoints.remove(lineSegmentsAsPoints[index])
    mapMatrix = []
    for rowIndex in range(MAX_VALUE):
        row = []
        for columnINDEX in range(MAX_VALUE):
            row += [0]
        mapMatrix += [row]
    index = 0
    for line in lineSegmentsAsPoints:
        firstPoint = line[0]
        secondPoint = line[1]
        if firstPoint[0] == secondPoint[0]:
            if firstPoint[1] < secondPoint[1]:
                for index in range(firstPoint[1], secondPoint[1] + 1):
                    mapMatrix[firstPoint[0]][index] += 1
            else:
                for index in range(secondPoint[1], firstPoint[1] + 1):
                    mapMatrix[firstPoint[0]][index] += 1
        elif firstPoint[1] == secondPoint[1]:
            if firstPoint[0] < secondPoint[0]:
                for index in range(firstPoint[0], secondPoint[0] + 1):
                    mapMatrix[index][firstPoint[1]] += 1
            else:
                for index in range(secondPoint[0], firstPoint[0] + 1):
                    mapMatrix[index][firstPoint[1]] += 1

    atLeast2PointsOverlap = 0
    for row in mapMatrix:
        for number in row:
            if number >= 2:
                atLeast2PointsOverlap += 1
    return atLeast2PointsOverlap


def part2():
    # GAVE UP
    # inputValues = common.getInputOfDay(5)
    # lineSegments = [line.split(" -> ") for line in inputValues]
    # lineSegmentsAsPoints = [
    #     [common.getLineOfNumbersAsInts(lineSegment[0], ','), common.getLineOfNumbersAsInts(lineSegment[1], ',')] for
    #     lineSegment in lineSegments]
    # index = 0
    # while index < len(lineSegmentsAsPoints):
    #     firstPoint = lineSegmentsAsPoints[index][0]
    #     secondPoint = lineSegmentsAsPoints[index][1]
    #     if firstPoint[0] == secondPoint[0] or firstPoint[1] == secondPoint[1] or (firstPoint[0] == secondPoint[1] and firstPoint[1] == secondPoint[0]) or (
    #             firstPoint[0] == firstPoint[1] and secondPoint[1] == secondPoint[0]):
    #         index += 1
    #     else:
    #         lineSegmentsAsPoints.remove(lineSegmentsAsPoints[index])
    # mapMatrix = []
    # for rowIndex in range(MAX_VALUE):
    #     row = []
    #     for columnINDEX in range(MAX_VALUE):
    #         row += [0]
    #     mapMatrix += [row]
    # index = 0
    # for line in lineSegmentsAsPoints:
    #     firstPoint = line[0]
    #     secondPoint = line[1]
    #     if firstPoint[0] == secondPoint[0]:
    #         if firstPoint[1] < secondPoint[1]:
    #             for index in range(firstPoint[1], secondPoint[1] + 1):
    #                 mapMatrix[firstPoint[0]][index] += 1
    #         else:
    #             for index in range(secondPoint[1], firstPoint[1] + 1):
    #                 mapMatrix[firstPoint[0]][index] += 1
    #     elif firstPoint[1] == secondPoint[1]:
    #         if firstPoint[0] < secondPoint[0]:
    #             for index in range(firstPoint[0], secondPoint[0] + 1):
    #                 mapMatrix[index][firstPoint[1]] += 1
    #         else:
    #             for index in range(secondPoint[0], firstPoint[0] + 1):
    #                 mapMatrix[index][firstPoint[1]] += 1
    #     elif firstPoint[0] == secondPoint[1]:
    #         if firstPoint[0] < secondPoint[0]:
    #             for index in range(firstPoint[0], secondPoint[0] + 1):
    #                 mapMatrix[index][firstPoint[1]] += 1
    #         else:
    #             for index in range(secondPoint[0], firstPoint[0] + 1):
    #                 mapMatrix[index][firstPoint[1]] += 1
    #     elif firstPoint[1] == secondPoint[0]:
    #         if firstPoint[0] < secondPoint[0]:
    #             for index in range(firstPoint[0], secondPoint[0] + 1):
    #                 mapMatrix[index][firstPoint[1]] += 1
    #         else:
    #             for index in range(secondPoint[0], firstPoint[0] + 1):
    #                 mapMatrix[index][firstPoint[1]] += 1
    #     elif firstPoint[0] == firstPoint[1]:
    #         if firstPoint[0] < secondPoint[0]:
    #             for index in range(firstPoint[0], secondPoint[0] + 1):
    #                 mapMatrix[index][index] += 1
    #         else:
    #             for index in range(secondPoint[0], firstPoint[0] + 1):
    #                 mapMatrix[index][index] += 1
    #
    # atLeast2PointsOverlap = 0
    # for row in mapMatrix:
    #     for number in row:
    #         if number >= 2:
    #             atLeast2PointsOverlap += 1
    # return atLeast2PointsOverlap
    segments = [line.replace(' -> ', ',') for line in common.getInput()]

    straight, diagonal = [], []
    for line in segments:
        x1, y1, x2, y2 = map(int, line.split(','))
        (x1, y1), (x2, y2) = sorted([(x1, y1), (x2, y2)])
        if x1 == x2 or y1 == y2:
            straight += [(x, y) for x in range(x1, x2 + 1) for y in range(y1, y2 + 1)]
        elif y1 < y2:
            diagonal += [(x, y1 + idx) for idx, x in enumerate(range(x1, x2 + 1))]
        else:
            diagonal += [(x, y1 - idx) for idx, x in enumerate(range(x1, x2 + 1))]
    position_counts = Counter(straight)
    position_counts += Counter(diagonal)
    return sum(v > 1 for v in position_counts.values())

def main():
    print(part2())


if __name__ == "__main__":
    main()
