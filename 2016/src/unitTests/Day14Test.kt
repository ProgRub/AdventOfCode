import days.Day14
import org.junit.jupiter.api.BeforeEach

class Day14Test : DayTester() {

    @BeforeEach
    override fun setUp() {
        testDay = 14
        part1Solution = "35186"
        part2Solution = "22429"
        super.setUp()
        solver = Day14(inputText)
    }
}