import common
import statistics


def part1():
    inputValues = common.getInput()
    crabPositions = common.getLineOfNumbersAsInts(inputValues[0], ',')
    median = statistics.median(crabPositions)
    totalAmountOfFuel = 0
    for crabPosition in crabPositions:
        totalAmountOfFuel += abs(crabPosition - median)
    return totalAmountOfFuel


def part2():
    inputValues = common.getInput()
    crabPositions = common.getLineOfNumbersAsInts(inputValues[0], ',')
    mean = int(statistics.mean(crabPositions))
    totalAmountOfFuel = 0
    for crabPosition in crabPositions:
        totalAmountOfFuel += sum(range(abs(crabPosition - mean)+1))
    return totalAmountOfFuel


def main():
    print(part2())


if __name__ == "__main__":
    main()
