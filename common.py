def getInputOfDay(day):
    with open("inputs\day{}.txt".format(day)) as f:
        return [line.strip() for line in f if line.strip()]


def getLineOfNumbersAsInts(line, separator):
    return [int(number) for number in line.split(separator)]


def getGridFromLinesOfUninterruptedInts(lines):
    grid = []
    for line in lines:
        row = []
        for character in line:
            row += [int(character)]
        grid += [row]
    return grid
