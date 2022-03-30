# file to create input files and python files for each day

for year in range(2015, 2022):
    for day in range(1,26):
        print(f"{year}\\inputs\\day{day}.txt")
        print(f"{year}\\day{day}.py")
