from common import common
import json

exceptionString = "red"


def part1():
    inputValues = common.getInput()
    sum = 0
    curNumber = ""
    digits = "0123456789-"
    # Go through every character, from right to left
    # If it's a number, we start building the full number
    # As soon as we hit a non-numeric character, it means the number is done and we can add it to the sum
    for char in inputValues[0][::-1]:
        if char in digits:
            curNumber = char + curNumber
        else:
            sum += int(curNumber if curNumber != "" else 0)
            curNumber = ""
    return sum


# The objective of this function is to get the sum of each element to add to the total
# It's a depth first search, we will sum the first element in the param to the end OR until we hit the exception string
# If we hit the exception string we stop, discarding the sum of the element and all its' children and head back up
# It should return the total sum of the element
def getSum(element) -> int:
    sum = 0
    match element:
        case dict():
            jsonDict = dict(element)
            for value in jsonDict.values():
                # If any of the values equals the exception string reset the sum and break because the object and it's children are ignored
                if type(value) is str and value == exceptionString:
                    sum = 0
                    break
                sum += getSum(value)
        # If it's a list its' sum can't be discarded, even if it has the exception string
        case list():
            theList = list(element)
            for value in theList:
                sum += getSum(value)
        case int():
            sum += int(element)
        case _:
            return 0
    return sum


def part2():
    inputValues = common.getInput()
    jsonAll = json.loads(inputValues[0])
    sum = 0
    for element in jsonAll:
        sum += getSum(element)
    return sum


def main():
    print(part2())


if __name__ == "__main__":
    main()
