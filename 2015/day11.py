import sys

# setting path
sys.path.append("..")
# importing
from common import common
from itertools import pairwise

zValue = ord("z")
dividingValue = zValue + 1
aValue = ord("a")


# Function that checks if the password passed as param meets the three requirements specified
def passesRequirements(password: str) -> bool:
    pairs = [(first, second) for first, second in pairwise(password) if first == second]
    # Order of checs: Second > First > Third
    if (
        # Second requirement: Password cannot have the letters 'i', 'o', or 'l'. Confusing characters
        "i" not in password
        and "o" not in password
        and "l" not in password
        # First requirement: Password must include, at least, one 'straight' of three or more letters. Ex.: 'abc', 'xyz', 'cdef'
        and any(
            [
                ord(password[index]) + 1 == ord(password[index + 1])
                and ord(password[index + 1]) + 1 == ord(password[index + 2])
                for index, _ in enumerate(password[: len(password) - 2])
            ]
        )
        # Third requirement: Password must contain, at least, two different and non-overlapping pairs of letters. Ex.: 'aa' and 'bb', 'cc' and 'zz'
        and (len(pairs) > 1
        and pairs[0] != pairs[1])
    ):
        return True
    return False


# Function that generates the next possible password, 'incrementing' the letters.
# With 'a' being the first number and 'z' being the last number before incrementing the letter before.
def generatePassword(basePassword: str) -> str:
    newPassword = chr(max(((ord(basePassword[-1]) + 1) % dividingValue), aValue))
    incrementNext = newPassword == "a"
    for char in basePassword[::-1][1:]:
        # print(char)
        if incrementNext:
            newPassword += chr(max(((ord(char) + 1) % dividingValue), aValue))
            incrementNext = newPassword[-1] == "a"
        else:
            newPassword += char
    # Reverse it
    return newPassword[::-1]


def part1():
    inputValues = common.getInput()
    basePassword = inputValues[0]
    newPassword = generatePassword(basePassword)
    while not passesRequirements(newPassword):
        print(newPassword)
        newPassword = generatePassword(newPassword)
    for character in newPassword:
        print(ord(character))
    return newPassword


def part2():
    inputValues = common.getInput()
    return inputValues


def main():
    print(part1())


if __name__ == "__main__":
    main()
