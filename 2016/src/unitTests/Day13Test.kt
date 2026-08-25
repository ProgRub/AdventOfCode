import days.Day13
import org.junit.jupiter.api.BeforeEach

class Day13Test : DayTester() {

    @BeforeEach
    override fun setUp() {
        testDay = 13
        part1Solution = "92"
        part2Solution = "124"
        super.setUp()
        solver = Day13(inputText)
    }
}