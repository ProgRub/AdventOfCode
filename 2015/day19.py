from common import common
import numpy as np

leeway = 3


# Function that converts the replacements to be made into a dict
def parseReplacements(lines: list[str]) -> list[tuple[str, str]]:
    replacements = []
    separator = " => "
    for line in lines:
        replacements += [
            (line.split(separator)[1], line.split(separator)[0])
        ]  # For part2, to reverse engineer, we flip the order
    return replacements


# This function that makes all replacements possible for the replacement wanted
# Adds to the list of valid molecules if it wasn't there because we need the distinct molecules:
def makeNewMolecules(
    replacement: tuple[str, str],
    molecule: str,
    startPosition: int,
    validMolecules: list[str],
):
    replacementPosition = molecule.find(replacement[0], startPosition)
    # Exit condition is if there's no more replacements to be made
    if replacementPosition == -1:
        return
    moleculeRight = molecule[startPosition:]
    moleculeLeft = molecule[:startPosition]
    # New molecule is the part that stays the same from previous calls plus the rest of the molecule with the replacement made
    # Since only one replacement can be made at a time
    newMolecule = moleculeLeft + moleculeRight.replace(
        replacement[0], replacement[1], count=1
    )
    # Only count the distinct molecules
    if newMolecule not in validMolecules:
        validMolecules += [newMolecule]
    # Check the molecules made from making the same replacement further along the molecule
    makeNewMolecules(replacement, molecule, replacementPosition + 1, validMolecules)


# This function takes the target molecule and recursively walks back until we get to an electron
# Always taking the biggest chunk out possible
def reverseEngineerMolecule(
    replacement: tuple[str, str],
    replacements: list[tuple[str, str]],
    molecule: str,
    startPosition: int,
    stepsTaken: int,
    maxStepsAllowed: int,
) -> int:
    stepsTaken += 1
    replacementPosition = molecule.rfind(replacement[0], startPosition)
    # Exit condition is if there's no more replacements to be made
    if replacementPosition == -1:
        return 10000
    print(molecule)
    # Exit condition no more steps to take
    if stepsTaken >= maxStepsAllowed:
        return 10000
    moleculeRight = molecule[startPosition:]
    moleculeLeft = molecule[:startPosition]
    # New molecule is the part that stays the same from previous calls plus the rest of the molecule with the replacement made
    # Since only one replacement can be made at a time
    newMolecule = (
        molecule[:replacementPosition]
        + replacement[1]
        + molecule[replacementPosition + len(replacement[0]) :]
    )
    if newMolecule == "e":
        return stepsTaken
    remaindingReplacements = replacements.copy()
    try:
        remaindingReplacements.remove(replacement)
    except ValueError:
        pass
    # Check the molecules made from making the same replacement further along the molecule
    steps = reverseEngineerMolecule(
        replacement, remaindingReplacements, newMolecule, 0, stepsTaken, maxStepsAllowed
    )
    maxStepsAllowed = min(steps, maxStepsAllowed)
    # Apply the same process to the remainding replacements
    for r in remaindingReplacements:
        steps = reverseEngineerMolecule(
            r, remaindingReplacements, newMolecule, 0, stepsTaken, maxStepsAllowed
        )
        maxStepsAllowed = min(steps, maxStepsAllowed)
    return maxStepsAllowed


# This function finds the fewest number of steps needed to make the target molecule from the list of replacements
# It returns the number of steps taken to make the molecule
def makeTargetMolecule(
    replacementToMake: tuple[str, str],
    replacements: list[tuple[str, str]],
    currentMolecule: str,
    startPosition: int,
    targetMolecule: str,
    stepsTaken: int,
    maxStepsAllowed: int,
) -> int:
    print(f"{currentMolecule} {stepsTaken}")
    stepsTaken += 1
    replacementPosition = currentMolecule.find(replacementToMake[0], startPosition)
    moleculeRight = currentMolecule[startPosition:]
    moleculeLeft = currentMolecule[:startPosition]
    # New molecule is the part that stays the same from previous calls plus the rest of the molecule with the replacement made
    # Since only one replacement can be made at a time
    newMolecule = moleculeLeft + moleculeRight.replace(
        replacementToMake[0], replacementToMake[1], count=1
    )
    # Success exit condition: check if we reached the target molecule, return the amount of steps taken to it
    if newMolecule == targetMolecule:
        return stepsTaken
    # Failure exit condition #1: if the amount of steps taken is above the minimum found no point in checking
    if stepsTaken >= maxStepsAllowed:
        return 10000
    # Failure exit condition #2: if there are no more replacements to be made, we couldn't reach the target molecule
    if replacementPosition == -1:
        return 10000
    # Failure exit condition #3: if the new molecule is too different from the target, there's no point checking
    # We check the target molecule in chunks equal to the length of the new molecule
    start = 0
    end = len(newMolecule)
    passes = False
    asciiMolecule = [ord(char) for char in newMolecule]
    while end < len(targetMolecule):
        asciiTarget = [ord(char) for char in targetMolecule[start:end]]
        if (
            np.sum(asciiMolecule) <= np.sum(asciiTarget) + leeway
            and np.sum(asciiMolecule) >= np.sum(asciiTarget) - leeway
        ):
            passes = True
            break
        start = end
        end += len(newMolecule)
    if not passes:
        return 20000
    # Failure exit condition #4: if the new molecule is bigger than the target one we can stop checking
    if len(newMolecule) >= len(targetMolecule):
        return 10000
    # If we can make the same replacement further along in the molecule, check those recursively to check if we reach the target molecule
    """ while True:
        replacementPosition = moleculeRight.find(replacementToMake[0],replacementPosition+1)
        if replacementPosition == -1: break
        steps = makeTargetMolecule(
            replacementToMake,
            replacements,
            currentMolecule,
            replacementPosition,
            targetMolecule,
            stepsTaken,
            maxStepsAllowed,
        )
        maxStepsAllowed = min(steps,maxStepsAllowed)
    # Success exit condition: check if we reached the target molecule, return the amount of steps taken to it
    if newMolecule == targetMolecule:
        return stepsTaken """
    # After checking the rest of the molecule, we now need to check the next replacements to make in the new molecule with the replacement made
    currentMolecule = newMolecule
    # Call recursively using the replacements that have an effect
    for next in [r for r in replacements if r[0] in currentMolecule]:
        steps = makeTargetMolecule(
            next,
            replacements,
            currentMolecule,
            0,
            targetMolecule,
            stepsTaken,
            maxStepsAllowed,
        )
        maxStepsAllowed = min(steps, maxStepsAllowed)
        # if steps equals 20000 then it's because the molecule is too far apart from the target, no point in continuing
        if steps == 20000:
            break
    return max(stepsTaken, maxStepsAllowed)


# Function to do it with the test data to understand the goal
def test():
    inputValues = common.getInput()
    # Test part 1
    replacements = parseReplacements(inputValues[:-1].tolist())
    molecule = inputValues[-1]
    newDistinctMolecules = []
    for replace in replacements:
        makeNewMolecules(replace, molecule, 0, newDistinctMolecules)
    # Test part 2
    targetMolecule = inputValues[-1]
    minimumStepsToTarget = 10000
    # We have to start from an electron
    moleculeStart = "e"
    """ for replace in [r for r in replacements if r[0] == "e"]:
        minimumStepsToTarget = makeTargetMolecule(
            replace,
            replacements,
            moleculeStart,
            0,
            targetMolecule,
            0,
            minimumStepsToTarget,
        ) """
    replacements.sort(key=lambda a: len(a[0]), reverse=True)
    for replace in replacements:
        steps = reverseEngineerMolecule(
            replace, replacements, targetMolecule, 0, 0, minimumStepsToTarget
        )
        minimumStepsToTarget = min(steps, minimumStepsToTarget)
    return minimumStepsToTarget


def part1():
    inputValues = common.getInput()
    replacements = parseReplacements(inputValues[:-1].tolist())
    molecule = inputValues[-1]
    newDistinctMolecules = []
    for replace in replacements:
        makeNewMolecules(replace, molecule, 0, newDistinctMolecules)
    return len(newDistinctMolecules)


def part2():
    inputValues = common.getInput()
    replacements = parseReplacements(inputValues[:-1].tolist())
    targetMolecule = inputValues[-1]
    minimumStepsToTarget = 1000
    replacements.sort(key=lambda a: len(a[0]), reverse=True)
    for replace in replacements:
        steps = reverseEngineerMolecule(
            replace, replacements, targetMolecule, 0, 0, minimumStepsToTarget
        )
        minimumStepsToTarget = min(steps, minimumStepsToTarget)
    return minimumStepsToTarget


def main():
    print(part2())


if __name__ == "__main__":
    main()
