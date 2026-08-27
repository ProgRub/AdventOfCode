import days.Day18
import org.junit.jupiter.api.BeforeEach

class Day18Test : DayTester() {

    @BeforeEach
    override fun setUp() {
        testDay = 18
        part1Solution = "1989"
        part2Solution = "19999894"
        super.setUp()
        solver = Day18(inputText)
    }
}