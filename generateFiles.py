import sys
from os.path import exists
from pathlib import Path


def createFiles(startYear, endYear):
    for year in range(startYear, endYear):
        if not exists(str(year)):
            Path(f"{year}\\inputs").mkdir(parents=True, exist_ok=True)
        for day in range(1, 26):
            textFilename = f"{year}\\inputs\\day{day}.txt"
            pythonFilename = f"{year}\\day{day}.py"
            if exists(textFilename) and exists(pythonFilename):
                continue
            textFile = open(textFilename, "w")
            textFile.write("Input here.")
            textFile.close()

            pythonFile = open(pythonFilename, "w")
            pythonFile.write("import common\n\n\n")
            pythonFile.write("def part1():\n    inputValues = common.getInput()\n    return inputValues\n\n\n")
            pythonFile.write("def part2():\n    inputValues = common.getInput()\n    return inputValues\n\n\n")
            pythonFile.write("def main():\n    print(part1())\n\n\n")
            pythonFile.write("if __name__ == \"__main__\":\n    main()")
            pythonFile.close()


if __name__ == "__main__":
    arguments = sys.argv[1:]
    if len(arguments) == 1:
        createFiles(int(arguments[0]), int(arguments[0]) + 1)
    else:
        createFiles(int(arguments[0]), int(arguments[1])+1)
