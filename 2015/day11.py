import sys

# setting path
sys.path.append("..\\AdventOfCode")

# importing
import common

zValue = ord("z")
aValue = ord("a")


# Function that checks if the password passed as param meets the three requirements specified
def passesRequirements(password: str) -> bool:
    # First requirement: Password must include, at least, one 'straight' of three or more letters. Ex.: 'abc', 'xyz', 'cdef'
    if all(
        [
            ord(password[index]) + 1 != ord(password[index + 1])
            or ord(password[index + 1]) + 1 != ord(password[index + 2])
            for index, _ in enumerate(password[: len(password) - 2])
        ]
    ):
        return False
    # Second requirement: Password cannot have the letters 'i', 'o', or 'l'. Confusing characters
    if "i" in password or "o" in password or "l" in password:
        return False
    # Third requirement: Password must contain, at least, two different and non-overlapping pairs of letters. Ex.: 'aa' and 'bb', 'cc' and 'zz'
    return True


# Function that generates the next possible password, 'incrementing' the letters.
# With 'a' being the first number and 'z' being the last number before incrementing the letter before.
def generatePassword(basePassword: str) -> str:
    newPassword = chr(max(((ord(basePassword[-1]) + 1) % zValue), aValue))
    incrementNext = newPassword == "a"
    for char in basePassword[::-1][1:]:
        if incrementNext:
            newPassword += chr(max(((ord(char) + 1) % zValue), aValue))
        else:
            newPassword += char
        incrementNext = char == "z"
    return newPassword[::-1]


def part1():
    inputValues = common.getInput()
    basePassword = inputValues[0]
    newPassword = generatePassword(basePassword)
    while not passesRequirements(newPassword):
        newPassword = generatePassword(basePassword)
    return newPassword


def part2():
    inputValues = common.getInput()
    return inputValues


def main():
    print(part1())


if __name__ == "__main__":
    main()
