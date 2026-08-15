from common import common

liters = 150


def divideLiters(cont: int, containers: list[int], remaining: int) -> int:
    remaining -= cont
    # Remove the used container
    if containers != []:
        containers.remove(cont)
    # If we've perfectly filled the container it's a valid combination
    if remaining == 0:
        return 1
    # If there's no more containers left and we still have some remaining it's an invalid combination
    elif containers == []:
        return 0
    # Only makes sense to check the containers that can be filled up by the remaining, i.e. smaller or equal
    validContainers = [c for c in containers if c <= remaining]
    if validContainers != []:
        combinations = 0
        # It's sorted form smallest to largest, start by filling largest containers first
        for conti in validContainers[::-1]:
            combinations += divideLiters(conti, validContainers.copy(), remaining)
            # Since we already have explored all possible combinations for the container, remove it from the pool
            validContainers.remove(conti)
        return combinations
    # If there are no valid possible containers then we can't fully fill one, invalid combination
    else:
        return 0


# How many different combinations to divide the liters
def part1():
    inputValues = common.getInput()
    possibleCombinations = 0
    containers = [int(line) for line in inputValues]
    containers.sort()
    print(containers)
    # It's sorted form smallest to largest, start by filling largest containers first
    for cont in containers[::-1]:
        possibleCombinations += divideLiters(cont, containers.copy(), liters)
        print(
            f"Starting with container {cont} possible combinations is now: {possibleCombinations}"
        )
        # Since we already have explored all possible combinations for the container, remove it from the pool
        containers.remove(cont)
    return possibleCombinations


# How many different combinations using the minimum amount of containers
def part2():
    inputValues = common.getInput()
    possibleCombinations = 0
    containers = [int(line) for line in inputValues]
    containers.sort()
    print(containers)
    # It's sorted form smallest to largest, start by filling largest containers first
    for cont in containers[::-1]:
        possibleCombinations += divideLiters(cont, containers.copy(), liters)
        print(
            f"Starting with container {cont} possible combinations is now: {possibleCombinations}"
        )
        # Since we already have explored all possible combinations for the container, remove it from the pool
        containers.remove(cont)
    return possibleCombinations


def main():
    print(part1())


if __name__ == "__main__":
    main()
