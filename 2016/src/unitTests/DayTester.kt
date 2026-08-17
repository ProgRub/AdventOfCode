import common.FileReader
import days.IDaySolver
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.nio.file.Paths
import kotlin.properties.Delegates

open class DayTester {
    lateinit var  part1Solution: String
    lateinit var  part2Solution: String
    var testDay by Delegates.notNull<Int>()
    lateinit var inputText: List<String>
    lateinit var solver: IDaySolver

    @BeforeEach
    open fun setUp() {
        val cwd = Paths.get("").toAbsolutePath().toString()
        val inputsFolder = "inputs/"
        val fileReader = FileReader(cwd.substring(0, cwd.lastIndexOf('/') + 1) + inputsFolder, testDay)
        inputText = fileReader.parseFile()
    }

    @Test
    @DisplayName("Part 1 test")
    fun part1test(){
        Assertions.assertEquals(part1Solution, solver.part1())
    }

    @Test
    @DisplayName("Part 2 test")
    fun part2test(){
        Assertions.assertEquals(part2Solution, solver.part2())
    }
}