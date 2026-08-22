import common.FileReader
import days.*
import java.nio.file.Paths

fun main() {
    val cwd = Paths.get("").toAbsolutePath().toString()
    val inputsFolder = "inputs/"

    val dayProblem = 9
    val partSolve = 2 // 0 - test; 1 - part 1; 2 - part 2
    val fileReader = FileReader(cwd.substring(0, cwd.lastIndexOf('/') + 1) + inputsFolder, dayProblem)
    val inputText = fileReader.parseFile()
    lateinit var daySolver: IDaySolver
    when (dayProblem) {
        1 -> daySolver = Day1(inputText)
        2 -> daySolver = Day2(inputText)
        3 -> daySolver = Day3(inputText)
        4 -> daySolver = Day4(inputText)
        5 -> daySolver = Day5(inputText)
        6 -> daySolver = Day6(inputText)
        7 -> daySolver = Day7(inputText)
        8 -> daySolver = Day8(inputText)
        9 -> daySolver = Day9(inputText)
        10 -> daySolver = Day10(inputText)
        11 -> daySolver = Day11(inputText)
        12 -> daySolver = Day12(inputText)
        13 -> daySolver = Day13(inputText)
        14 -> daySolver = Day14(inputText)
        15 -> daySolver = Day15(inputText)
        16 -> daySolver = Day16(inputText)
        17 -> daySolver = Day17(inputText)
        18 -> daySolver = Day18(inputText)
        19 -> daySolver = Day19(inputText)
        20 -> daySolver = Day20(inputText)
        21 -> daySolver = Day21(inputText)
        22 -> daySolver = Day22(inputText)
        23 -> daySolver = Day23(inputText)
        24 -> daySolver = Day24(inputText)
        25 -> daySolver = Day25(inputText)
    }
    when (partSolve) {
        0 -> println(daySolver.test())
        1 -> println( daySolver.part1())
        2 -> println( daySolver.part2())
    }
}