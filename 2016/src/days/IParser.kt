package days

interface IParser {
    fun parseTextToList(line: String, separator: String): List<String> = line.split(separator).map(String::trim)
    fun parseTextToGrid(lines: List<String>, separator: String): List<List<String>> =
        lines.map { parseTextToList(it, separator) }

    fun parseTextToMap(lines: List<String>, separator: String): Map<String, String> {
        val result = mutableMapOf<String, String>()
        for (line in lines) {
            result[line.split(separator)[0]] = line.split(separator)[1]
        }
        return result
    }
}