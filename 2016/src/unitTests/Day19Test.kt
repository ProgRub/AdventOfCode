import days.Day19
import org.junit.jupiter.api.BeforeEach

private class Day19Test : DayTester() {

    @BeforeEach
    override fun setUp() {
        testDay = 19
        part1Solution = "1808357"
        part2Solution = "1407007"
        super.setUp()
        solver = Day19(inputText)
    }
}