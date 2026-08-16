package common

import java.io.File

interface IFileParser {
    fun parseFileToLinesList(file: File): List<String>
}