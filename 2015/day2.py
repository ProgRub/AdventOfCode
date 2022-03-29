import common


def part1():
    inputValues = common.getInputOfDay(2)
    totalWrappingPaper = 0
    for box in inputValues:
        measures = box.split('x')
        length = int(measures[0])
        width = int(measures[1])
        height = int(measures[2])
        surfaceArea = 2 * length * width + 2 * width * height + 2 * height * length
        extra = min(length * width, width * height, height * length)
        totalWrappingPaper += surfaceArea + extra
    return totalWrappingPaper


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
    print(part2())


if __name__ == "__main__":
    main()
