import common


def part1():
    inputValues = common.getInputOfDay(6)
    DAYS_TO_SIMULATE = 80
    fish = common.getLineOfNumbersAsInts(inputValues[0], ',')
    for _ in range(DAYS_TO_SIMULATE):
        for index in range(len(fish)):
            if fish[index] == 0:
                fish[index] = 6
                fish.append(8)
            else:
                fish[index] -= 1
    return len(fish)


def part2():
    inputValues = common.getInputOfDay(6)
    DAYS_TO_SIMULATE = 256
    CHUNK_SIZE=5
    totalFish = common.getLineOfNumbersAsInts(inputValues[0], ',')
    chunksOfFish = [totalFish[x:x + CHUNK_SIZE] for x in range(0, len(totalFish), CHUNK_SIZE)]
    for _ in range(DAYS_TO_SIMULATE):
        for chunkIndex in range(len(chunksOfFish)):
            for index in range(len(chunksOfFish[chunkIndex])):
                if chunksOfFish[chunkIndex][index] == 0:
                    chunksOfFish[chunkIndex][index] = 6
                    chunksOfFish[chunkIndex].append(8)
                else:
                    chunksOfFish[chunkIndex][index] -= 1
    sum = 0
    for chunk in chunksOfFish:
        sum += len(chunk)
    return sum


def main():
    print(part2())


if __name__ == "__main__":
    main()
