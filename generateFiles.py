# file to create input files and python files for each day

file = open("example.py", "w")
file.write("import common\n\n\n")
file.write("def part1():\n    inputValues = common.getInputOfDay()\n    return inputValues\n\n\n")
file.write("def part2():\n    inputValues = common.getInputOfDay()\n    return inputValues\n\n\n")
file.write("def main():\n    print(part1())\n\n\n")
file.write("if __name__ == \"__main__\":\n    main()")

for year in range(2015, 2022):
    for day in range(1, 26):
        print(f"{year}\\inputs\\day{day}.txt")
        print(f"{year}\\day{day}.py")
