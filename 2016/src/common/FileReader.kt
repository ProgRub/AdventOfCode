package common

import java.io.File
import kotlin.io.path.Path

class FileReader : IFileParser {
    val inputsFolder: String
    val day: Int
    var inputFile: File

    constructor(inputsFolder: String, day: Int) {
        this.inputsFolder = inputsFolder
        this.day = day
        this.inputFile = File(Path(inputsFolder, "day${day}.txt").toString())
    }

    fun parseFile(): List<String> = parseFileToLinesList(inputFile)

    override fun parseFileToLinesList(file: File): List<String> = file.readLines()

}