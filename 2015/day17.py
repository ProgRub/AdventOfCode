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
        # Traverse in reverse so we start by filling largest containers first
        for conti in validContainers[::-1]:
            combinations += divideLiters(conti, validContainers.copy(), remaining)
            # Since we already have explored all possible combinations for the container, remove it from the pool
            validContainers.remove(conti)
        return combinations
    # If there are no valid possible containers then we can't fully fill one, invalid combination
    else:
        return 0


# A variation of the divide liters function, where we only count the possible combinations using the least amount of containers
def divideLitersMinimumContainers(
    cont: int, containers: list[int], remaining: int, containersUsed: int, maxUse: int
) -> tuple[int, int]:
    remaining -= cont
    containersUsed += 1
    # Remove the used container
    if containers != []:
        containers.remove(cont)
    # If we've perfectly filled the container it's a valid combination, counts if it's not over the minimum amount to make a combination
    if remaining == 0 and containersUsed <= maxUse:
        maxUse = min(maxUse, containersUsed)
        return 1, maxUse
    # If there's no more containers left and we still have some remaining it's an invalid combination
    elif containers == []:
        return 0, maxUse
    # If we already hit the maximum number of containers we can use, no point in checking, it's not a valid combination
    elif containersUsed == maxUse:
        return 0, maxUse
    # Only makes sense to check the containers that can be filled up by the remaining, i.e. smaller or equal
    validContainers = [c for c in containers if c <= remaining]
    if validContainers != []:
        combinations = 0
        # Traverse in reverse so we start by filling largest containers first
        for conti in validContainers[::-1]:
            combsFound, maxUse = divideLitersMinimumContainers(
                conti, validContainers.copy(), remaining, containersUsed, maxUse
            )
            combinations += combsFound
            # Since we already have explored all possible combinations for the container, remove it from the pool
            validContainers.remove(conti)
        return combinations, maxUse
    # If there are no valid possible containers then we can't fully fill one, invalid combination
    else:
        return 0, maxUse


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
    minimumContainersUsed = len(containers)
    print(containers)
    # It's sorted form smallest to largest, start by filling largest containers first
    for cont in containers[::-1]:
        combsFound, minimumContainersUsed = divideLitersMinimumContainers(
            cont, containers.copy(), liters, 0, minimumContainersUsed
        )
        possibleCombinations += combsFound
        print(
            f"Starting with container {cont} possible combinations is now: {possibleCombinations}"
        )
        # Since we already have explored all possible combinations for the container, remove it from the pool
        containers.remove(cont)
    return possibleCombinations


def main():
    print(part2())


if __name__ == "__main__":
    main()
