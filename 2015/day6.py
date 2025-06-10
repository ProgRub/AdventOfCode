import sys

# setting path
sys.path.append("..\\AdventOfCode")

# importing
import common
from dataclasses import dataclass
from enum import Enum

import numpy as np


class InstructionType(Enum):
    turnOn = 1
    turnOff = 2
    toggle = 3


@dataclass
class Instruction:
    instructionType: InstructionType
    startRow: int
    startColumn: int
    endRow: int
    endColumn: int


def parseInstruction(instructionsStrings):
    instructions = []
    for line in instructionsStrings:
        split = line.split()
        split.remove("through")
        instructionType = InstructionType.toggle
        if split[1] == "on":
            instructionType = InstructionType.turnOn
        elif split[1] == "off":
            instructionType = InstructionType.turnOff
        endCoordinates = split[-1]
        startCoordinates = split[-2]
        start_coordinates_split = startCoordinates.split(',')
        end_coordinates_split = endCoordinates.split(',')
        instruction = Instruction(instructionType, int(start_coordinates_split[0]), int(start_coordinates_split[1]), int(end_coordinates_split[0]), int(end_coordinates_split[1]))
        instructions.append(instruction)
    return instructions


def applyInstruction(lights, instruction, forPart1):
    for rowIndex in range(instruction.startRow, instruction.endRow + 1):
        for columnIndex in range(instruction.startColumn, instruction.endColumn + 1):
            if instruction.instructionType == InstructionType.toggle:
                if forPart1:
                    lights[rowIndex, columnIndex] = not lights[rowIndex, columnIndex]
                else:
                    lights[rowIndex, columnIndex] += 2
            elif instruction.instructionType == InstructionType.turnOn:
                if forPart1:
                    lights[rowIndex, columnIndex] = True
                else:
                    lights[rowIndex, columnIndex] += 1
            elif instruction.instructionType == InstructionType.turnOff:
                if forPart1:
                    lights[rowIndex, columnIndex] = False
                else:
                    lights[rowIndex, columnIndex] = max(lights[rowIndex, columnIndex] - 1, 0)


def part1():
    inputValues = common.getInput()
    instructions = parseInstruction(inputValues)
    lights = np.zeros((1000, 1000), dtype=bool)
    for instruction in instructions:
        applyInstruction(lights, instruction, True)
    return lights.sum()


def part2():
    inputValues = common.getInput()
    instructions = parseInstruction(inputValues)
    lights = np.zeros((1000, 1000), dtype=int)
    for instruction in instructions:
        applyInstruction(lights, instruction, False)
    return lights.sum()


def main():
    print(part2())


if __name__ == "__main__":
    main()
