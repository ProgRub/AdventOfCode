import common


def part1():
    inputValues = common.getInput()
    increaseCounter = 0
    for index in range(1, len(inputValues)):
        if int(inputValues[index]) > int(inputValues[index - 1]):
            increaseCounter += 1
    return increaseCounter


def part2():
    inputValues = common.getInput()
    increaseCounter = 0
    for index in range(3, len(inputValues)):
        if int(inputValues[index]) > int(inputValues[index - 3]):
            increaseCounter += 1
    return increaseCounter


def main():
    print(part1())


if __name__ == "__main__":
    main()
