import common


def part1():
    inputValues = common.getInput()
    uniqueDigits = 0
    for line in inputValues:
        outputValues = line.split(" | ")[1]
        for value in outputValues.split(" "):
            if len(value) == 2 or len(value) == 4 or len(value) == 3 or len(value) == 7:
                uniqueDigits += 1
    return uniqueDigits


def getDigitSequences(inputs):
    digits = {}
    split = inputs.split(" ")
    split.sort(key=lambda s: len(s))
    for input in split:
        if len(input) == 2:
            digits[1] = input
        elif len(input) == 4:
            digits[4] = input
        elif len(input) == 3:
            digits[7] = input
        elif len(input) == 7:
            digits[8] = input
        else:
            if len(input) == 6:  # 0,6 or 9
                if all([character in input for character in digits[4]]):
                    digits[9] = input
                elif all([character in input for character in digits[1]]):
                    digits[0] = input
                else:
                    digits[6] = input
            #     if digits[1] not in input: if len([character for character in input if character not in digits[1]])==0:
            #         digits[6] = input
            # elif digits[4] not in input:
            #     else:
            #         digits[9] = input
            elif len(input) == 5:  # 2, 3 or 5
                if all([character in input for character in digits[1]]):
                    digits[3] = input
                else:
                    commonSegmentsWith4 = 0
                    for segment in digits[4]:
                        if segment in input:
                            commonSegmentsWith4 += 1
                    if commonSegmentsWith4 == 3:
                        digits[5] = input
                    else:
                        digits[2] = input
    return digits


def part2():
    inputValues = common.getInput()
    sum = 0
    for line in inputValues:
        split = line.split(" | ")
        inputs = split[0].strip()
        outputs = split[1].strip()
        digits = getDigitSequences(inputs)
        # print(digits)
        multiplier = 1000
        number = 0
        for outputDigit in outputs.split(" "):
            for key, value in digits.items():
                if len(outputDigit) == len(value) and len([character for character in outputDigit if character not in value]) == 0:
                    number += key * multiplier
                    break
            multiplier = multiplier // 10
        # print(number)
        sum += number
    return sum


def main():
    print(part2())


if __name__ == "__main__":
    main()
