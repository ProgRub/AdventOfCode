import common
import numpy as np
from dataclasses import dataclass


@dataclass
class Wire:
    receiver: str
    operation: str


def parseWires(wiresStrings):
    return [Wire(receiver=wire.split(" -> ")[1], operation=wire.split(" -> ")[0]) for wire in wiresStrings]


def getNextWireUp(wires, variable):
    for wire in wires:
        if wire.receiver == variable:
            return wire


def getImportantWires(wires, startingVariable, importantWires, variables):
    wire = getNextWireUp(wires, startingVariable)
    if wire == None:
        return importantWires
    importantWires.append(wire)
    wires.remove(wire)
    operationSplit = importantWires[-1].operation.split()
    if len(operationSplit) == 1:
        if operationSplit[0].isdigit():
            variables[wire.receiver] = int(operationSplit[0])
            return importantWires
        else:
            importantWires = getImportantWires(wires, operationSplit[0], importantWires, variables)
    else:
        if operationSplit[0] == "NOT":
            importantWires = getImportantWires(wires, operationSplit[1], importantWires, variables)
        else:
            if not operationSplit[0].isdigit():
                importantWires = getImportantWires(wires, operationSplit[0], importantWires, variables)
            if not operationSplit[2].isdigit():
                importantWires = getImportantWires(wires, operationSplit[2], importantWires, variables)

    return importantWires


def part1():
    inputValues = common.getInput()
    wires = parseWires(inputValues)
    variables = {}
    importantWires = getImportantWires(wires, 'a', [], variables)
    print(variables)
    print(importantWires)
    while 'a' not in variables:
        for wire in importantWires:
            operationSplit = wire.operation.split()
            if len(operationSplit)==2:
                pass #not case
            else:
                if operationSplit[1]=="LSHIFT":
                    pass
                elif operationSplit[1]=="RSHIFT":
                    pass
                elif operationSplit[1]=="AND":
                    pass
                elif operationSplit[1]=="OR":
                    pass


    return variables['a']


def part2():
    inputValues = common.getInput()
    return inputValues


def main():
    print(np.uint16(456>>2))
    print(part1())


if __name__ == "__main__":
    main()
