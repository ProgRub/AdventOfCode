import common

BINGO_BOARD_SIZE = 5


def checkBingoBoardRows(bingoBoardMarks):
    return any([all(row) for row in bingoBoardMarks])


def checkBingoBoardColumns(bingoBoardMarks):
    columns = []
    for index in range(BINGO_BOARD_SIZE):
        columns += [[row[index] for row in bingoBoardMarks]]
    return any([all(column) for column in columns])


def doesBingoBoardWin(bingoBoardMarks):
    return checkBingoBoardRows(bingoBoardMarks) or checkBingoBoardColumns(bingoBoardMarks)


def fillInBingoBoard(bingoBoard, bingoBoardMarks, calledNumber):
    for rowIndex in range(BINGO_BOARD_SIZE):
        for numberIndex in range(BINGO_BOARD_SIZE):
            if bingoBoard[rowIndex][numberIndex] == calledNumber:
                bingoBoardMarks[rowIndex][numberIndex] = True
                return


def getBingoBoards(inputValues):
    bingoBoards = []
    index = 0
    for line in inputValues[1:]:
        if index % BINGO_BOARD_SIZE == 0:
            try:
                bingoBoards += [newBingoBoard]
            except UnboundLocalError:
                pass
            newBingoBoard = []
        newBingoBoard += [common.getLineOfNumbersAsInts(line, None)]
        index += 1
    return bingoBoards


def getBingoBoardsMarks(bingoBoards):
    bingoBoardsMarks = []
    for _ in range(len(bingoBoards)):
        board = []
        for _ in range(BINGO_BOARD_SIZE):
            row = []
            for _ in range(BINGO_BOARD_SIZE):
                row.append(False)
            board.append(row)
        bingoBoardsMarks.append(board)
    return bingoBoardsMarks


def part1():
    inputValues = common.getInputOfDay()
    calledNumbers = common.getLineOfNumbersAsInts(inputValues[0], ',')
    bingoBoards = getBingoBoards(inputValues)
    bingoBoardsMarks = getBingoBoardsMarks(bingoBoards)

    winningIndex = -1
    lastCalledNumber = -1
    for calledNumber in calledNumbers:
        for index in range(len(bingoBoards)):
            fillInBingoBoard(bingoBoards[index], bingoBoardsMarks[index], calledNumber)
            if doesBingoBoardWin(bingoBoardsMarks[index]):
                winningIndex = index
                lastCalledNumber = calledNumber
                sumUnmarkedNumbers = 0
                for rowIndex in range(BINGO_BOARD_SIZE):
                    for numberIndex in range(BINGO_BOARD_SIZE):
                        if not bingoBoardsMarks[winningIndex][rowIndex][numberIndex]:
                            sumUnmarkedNumbers += bingoBoards[winningIndex][rowIndex][numberIndex]
                return sumUnmarkedNumbers * lastCalledNumber


def part2():
    inputValues = common.getInputOfDay()
    calledNumbers = common.getLineOfNumbersAsInts(inputValues[0], ',')
    bingoBoards = getBingoBoards(inputValues)
    bingoBoardsMarks = getBingoBoardsMarks(bingoBoards)
    lastCalledNumber = -1
    for calledNumber in calledNumbers:
        index = 0
        while index < len(bingoBoards):
            fillInBingoBoard(bingoBoards[index], bingoBoardsMarks[index], calledNumber)
            if doesBingoBoardWin(bingoBoardsMarks[index]):
                lastCalledNumber = calledNumber
                if len(bingoBoards) == 1:
                    sumUnmarkedNumbers = 0
                    for rowIndex in range(BINGO_BOARD_SIZE):
                        for numberIndex in range(BINGO_BOARD_SIZE):
                            if not bingoBoardsMarks[0][rowIndex][numberIndex]:
                                sumUnmarkedNumbers += bingoBoards[0][rowIndex][numberIndex]
                    return sumUnmarkedNumbers * lastCalledNumber
                if len(bingoBoards) > 1:
                    bingoBoards.remove(bingoBoards[index])
                    bingoBoardsMarks.remove(bingoBoardsMarks[index])
            else:
                index += 1


def main():
    print(part2())


if __name__ == "__main__":
    main()
