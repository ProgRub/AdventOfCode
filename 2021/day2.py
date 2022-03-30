import common


def part1():
    inputValues = common.getInput()
    horizontal = 0
    depth = 0
    for value in inputValues:
        valueSplit = value.split()
        if valueSplit[0] == "forward":
            horizontal += int(valueSplit[1])
        elif valueSplit[0] == "down":
            depth += int(valueSplit[1])
        elif valueSplit[0] == "up":
            depth -= int(valueSplit[1])
    return horizontal * depth


def part2():
    inputValues = common.getInput()
    horizontal = 0
    aim = 0
    depth = 0
    for value in inputValues:
        valueSplit = value.split()
        if valueSplit[0] == "forward":
            horizontal += int(valueSplit[1])
            depth += int(valueSplit[1]) * aim
        elif valueSplit[0] == "down":
            aim += int(valueSplit[1])
        elif valueSplit[0] == "up":
            aim -= int(valueSplit[1])
    return horizontal * depth


def main():
    print(part2())


if __name__ == "__main__":
    main()
