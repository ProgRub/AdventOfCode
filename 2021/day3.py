import common


def part1():
    inputValues = common.getInputOfDay()
    lenBinaries = len(inputValues[0])
    gamma = '0' * lenBinaries
    epsilon = '0' * lenBinaries
    for index in range(lenBinaries):
        onesCount = 0
        for binary in inputValues:
            if binary[index] == '1':
                onesCount += 1
        if onesCount >= len(inputValues) / 2:
            aux = list(gamma)
            aux[index] = '1'
            gamma = "".join(aux)
            aux = list(epsilon)
            aux[index] = '0'
            epsilon = "".join(aux)
        else:
            aux = list(gamma)
            aux[index] = '0'
            gamma = "".join(aux)
            aux = list(epsilon)
            aux[index] = '1'
            epsilon = "".join(aux)
    return binaryToDecimal(gamma) * binaryToDecimal(epsilon)


def part2():
    inputValues = common.getInputOfDay()
    lenBinaries = len(inputValues[0])
    oxygen = '0'
    co2 = '0'
    for index in range(lenBinaries):
        bitAs1 = []
        bitAs0 = []
        for binary in inputValues:
            if binary[index] == '1':
                bitAs1 += [binary]
            else:
                bitAs0 += [binary]
        if len(bitAs1) >= len(bitAs0):
            oxygen = part2recursive(bitAs1, index + 1, True)
            co2 = part2recursive(bitAs0, index + 1, False)
        else:
            oxygen = part2recursive(bitAs0, index + 1, True)
            co2 = part2recursive(bitAs1, index + 1, False)
        if oxygen != None and co2 != None:
            break
    print(oxygen)
    print(co2)
    return binaryToDecimal(oxygen) * binaryToDecimal(co2)


def part2recursive(values, index, forOxygen):
    if len(values) == 1:
        return values[0]
    else:
        if index >= len(values[0]):
            return None
        bitAs1 = []
        bitAs0 = []
        for binary in values:
            if binary[index] == '1':
                bitAs1 += [binary]
            else:
                bitAs0 += [binary]
        if forOxygen:
            if len(bitAs1) >= len(bitAs0):
                return part2recursive(bitAs1, index + 1, forOxygen)
            else:
                return part2recursive(bitAs0, index + 1, forOxygen)
        else:
            if len(bitAs1) >= len(bitAs0):
                return part2recursive(bitAs0, index + 1, forOxygen)
            else:
                return part2recursive(bitAs1, index + 1, forOxygen)


def binaryToDecimal(binary):
    value = 0
    power = 0
    for index in range(len(binary) - 1, -1, -1):
        value += int(binary[index]) * pow(2, power)
        power += 1
    return value


def main():
    print(part2())


if __name__ == "__main__":
    main()
