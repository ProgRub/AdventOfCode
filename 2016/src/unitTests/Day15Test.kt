import days.Day15
import org.junit.jupiter.api.BeforeEach

private class Day15Test : DayTester() {

    @BeforeEach
    override fun setUp() {
        testDay = 15
        part1Solution = "121834"
        part2Solution = "3208099"
        super.setUp()
        solver = Day15(inputText)
    }
}