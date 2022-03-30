import common


def part1():
    inputValues = common.getInputOfDay()
    points = {
        ')': 3,
        ']': 57,
        '}': 1197,
        '>': 25137
    }
    startingChars = ['(', '[', '{', '<']
    endingChars = [')', ']', '}', '>']
    totalPoints = 0
    for line in inputValues:
        startingCharsInLine = []
        for char in line:
            if char in startingChars:
                startingCharsInLine += [char]
            elif char in endingChars:
                if startingCharsInLine[len(startingCharsInLine) - 1] == startingChars[endingChars.index(char)]:
                    startingCharsInLine.pop()
                else:
                    totalPoints += points[char]
                    break

    return totalPoints


def part2():
    inputValues = common.getInputOfDay()
    startingChars = ['(', '[', '{', '<']
    endingChars = [')', ']', '}', '>']
    corruptedLines = []
    for line in inputValues:
        startingCharsInLine = []
        for char in line:
            if char in startingChars:
                startingCharsInLine += [char]
            elif char in endingChars:
                if startingCharsInLine[len(startingCharsInLine) - 1] == startingChars[endingChars.index(char)]:
                    startingCharsInLine.pop()
                else:
                    corruptedLines.append(line)
                    break
    incompleteLines = [line for line in inputValues if line not in corruptedLines]
    points = {
        ')': 1,
        ']': 2,
        '}': 3,
        '>': 4
    }
    scores = []
    for line in incompleteLines:
        startingCharsInLine = []
        completion = []
        score = 0
        for char in line:
            if char in startingChars:
                startingCharsInLine += [char]
            elif char in endingChars:
                startingCharsInLine.pop()
        startingCharsInLine.reverse()
        for char in startingCharsInLine:
            completion += [endingChars[startingChars.index(char)]]
        for char in completion:
            score *= 5
            score += points[char]
        scores += [score]
    scores.sort()
    return scores[(len(incompleteLines)-1)//2]


def main():
    print(part2())


if __name__ == "__main__":
    main()
