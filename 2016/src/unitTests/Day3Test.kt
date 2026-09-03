import days.Day3
import org.junit.jupiter.api.BeforeEach

private class Day3Test : DayTester() {

    @BeforeEach
    override fun setUp() {
        testDay = 3
        part1Solution = "869"
        part2Solution = "1544"
        super.setUp()
        solver = Day3(inputText)
    }
}