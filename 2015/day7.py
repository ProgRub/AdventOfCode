import sys

# setting path
sys.path.append("..\\AdventOfCode")

# importing
import common

sys.path.append(".\\2015")

import numpy as np
from dataclasses import dataclass
from enum import Enum


class Operation(Enum):
    THROUGH = 0
    NOT = 1
    LSHIFT = 2
    RSHIFT = 3
    AND = 4
    OR = 5


@dataclass
class Gate:
    left: str
    right: str
    operation: Operation

    def __str__(self):
        match self.operation:
            case Operation.THROUGH:
                return f"{self.right}"
            case Operation.NOT:
                return f"NOT {self.right}"
            case _:
                return f"{self.left} {self.operation.name} {self.right}"
        return f"{self.gate} -> {self.receiver}"


@dataclass
class Wire:
    receiver: str
    value: np.int64
    gate: Gate

    def __str__(self):
        return f"{self.gate.__str__()} -> {self.receiver}"


def getGate(operation) -> Gate:
    operationSplit = operation.split()
    # If the first element is NOT then it is a NOT gate
    if operationSplit[0] == "NOT":
        return Gate("", operationSplit[1], Operation.NOT)
    # Otherwise if the length of the array is bigger than 1 it's a gate that takes two operands
    elif len(operationSplit) > 1:
        match operationSplit[1]:
            case "LSHIFT":
                return Gate(operationSplit[0], operationSplit[2], Operation.LSHIFT)
            case "RSHIFT":
                return Gate(operationSplit[0], operationSplit[2], Operation.RSHIFT)
            case "AND":
                return Gate(operationSplit[0], operationSplit[2], Operation.AND)
            case "OR":
                return Gate(operationSplit[0], operationSplit[2], Operation.OR)
    # If it doesn't return before this, then it's a THROUGH gate (result goes from a wire to the next)
    return Gate("", operationSplit[0], Operation.THROUGH)


# Builds an array of wires of a set of lines, respecting the format:
#   operation -> wire which will hold the result
def parseWires(wiresStrings):
    return [
        Wire(
            receiver=line.split(" -> ")[1], value=np.int64(0), gate=getGate(line.split(" -> ")[0])
        )
        for line in wiresStrings
    ]


# Get the next wire in which the receiver wire matches the variable
def getNextWireUp(wires: list[Wire], variable: str) -> Wire | None:
    for wire in wires:
        if wire.receiver == variable:
            return wire
    return None


# A recursive function which builds an array of all the operations
def getImportantWires(
    wires: list[Wire], startingVariable: str, importantWires: list[Wire]
) -> list[Wire]:
    wire = getNextWireUp(wires, startingVariable)
    # If no wire was returned then there's no more key wires
    if wire == None:
        return importantWires
    importantWires.append(wire)
    wires.remove(wire)  # have to remove it to not get stuck in a loop
    match wire.gate.operation:
        case Operation.THROUGH:
            importantWires = getImportantWires(wires, wire.gate.right, importantWires)
        case Operation.NOT:
            importantWires = getImportantWires(wires, wire.gate.right, importantWires)
        case Operation.LSHIFT:
            importantWires = getImportantWires(wires, wire.gate.left, importantWires)
        case Operation.RSHIFT:
            importantWires = getImportantWires(wires, wire.gate.left, importantWires)
        case Operation.AND:
            importantWires = getImportantWires(wires, wire.gate.left, importantWires)
            importantWires = getImportantWires(wires, wire.gate.right, importantWires)
        case Operation.OR:
            importantWires = getImportantWires(wires, wire.gate.left, importantWires)
            importantWires = getImportantWires(wires, wire.gate.right, importantWires)

    return importantWires


# A function that calculates the value of the wire, if possible, and adds it to the dictionary passed as param
def calculateWireValues(
    wire: Wire, wires: list[Wire], valuesDict: dict[str, np.int64]
) -> bool:
    if wire == None:
        return False
    match wire.gate.operation:
        case Operation.THROUGH:
            if wire.gate.right.isnumeric():
                wire.value = np.int64(wire.gate.right)
            else:
                try:
                    wire.value = np.int64(valuesDict[wire.gate.right])
                except KeyError:
                    return False
        case Operation.NOT:
            if wire.gate.right.isnumeric():
                wire.value = 65535 - np.int64(wire.gate.right)
            else:
                try:
                    wire.value = 65535 - np.int64(valuesDict[wire.gate.right])
                except KeyError:
                    return False
        case Operation.LSHIFT:
            try:
                wire.value = np.left_shift(
                    valuesDict[wire.gate.left], np.int64(wire.gate.right)
                )
            except KeyError:
                return False
        case Operation.RSHIFT:
            try:
                wire.value = np.right_shift(
                    valuesDict[wire.gate.left], np.int64(wire.gate.right)
                )
            except KeyError:
                return False
        case Operation.AND:
            try:
                wire.value = (
                    np.int64(wire.gate.left)
                    if wire.gate.left.isnumeric()
                    else valuesDict[wire.gate.left]
                ) & (
                    np.int64(wire.gate.right)
                    if wire.gate.right.isnumeric()
                    else valuesDict[wire.gate.right]
                )
            except KeyError:
                return False
            except StopIteration:
                return False
        case Operation.OR:
            try:
                wire.value = (
                    np.int64(wire.gate.left)
                    if wire.gate.left.isnumeric()
                    else valuesDict[wire.gate.left]
                ) | (
                    np.int64(wire.gate.right)
                    if wire.gate.right.isnumeric()
                    else valuesDict[wire.gate.right]
                )
            except KeyError:
                return False
            except StopIteration:
                return False
    valuesDict[wire.receiver] = wire.value
    return True


def part1():
    inputValues = common.getInput()
    wires = parseWires(inputValues)
    valuesDict = {}
    importantWires = getImportantWires(wires, "a", [])
    importantWires.sort(key=lambda x: (x.gate.operation.value, x.receiver))
    index = -1
    while len(importantWires) != 0:
        index = (index + 1) % len(importantWires)
        if calculateWireValues(importantWires[index], importantWires, valuesDict):
            importantWires.remove(importantWires[index])
            index -= 1
    return valuesDict['a']


def part2():
    inputValues = common.getInput()
    return inputValues


def main():
    print(part1())


if __name__ == "__main__":
    main()
