import common


def fishCalculation(fish, daysToSimulate):
    amountOfFish = {
        0: fish.count(0),
        1: fish.count(1),
        2: fish.count(2),
        3: fish.count(3),
        4: fish.count(4),
        5: fish.count(5),
        6: fish.count(6),
        7: fish.count(7),
        8: fish.count(8)
    }
    for day in range(daysToSimulate):
        amountOfFishNextDay = {
            0: amountOfFish[1],
            1: amountOfFish[2],
            2: amountOfFish[3],
            3: amountOfFish[4],
            4: amountOfFish[5],
            5: amountOfFish[6],
            6: amountOfFish[7],
            7: amountOfFish[8],
            8: amountOfFish[0]
        }
        amountOfFishNextDay[6] += amountOfFish[0]
        amountOfFish = amountOfFishNextDay
    return sum(amountOfFish.values())


def part1():
    inputValues = common.getInputOfDay()
    DAYS_TO_SIMULATE = 80
    fish = common.getLineOfNumbersAsInts(inputValues[0], ',')
    return fishCalculation(fish,DAYS_TO_SIMULATE)


def part2():
    inputValues = common.getInputOfDay()
    DAYS_TO_SIMULATE = 256
    totalFish = common.getLineOfNumbersAsInts(inputValues[0], ',')
    return fishCalculation(totalFish,DAYS_TO_SIMULATE)


def main():
    print(part2())


if __name__ == "__main__":
    main()
