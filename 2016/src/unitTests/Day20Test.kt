import days.Day20
import org.junit.jupiter.api.BeforeEach

private class Day20Test : DayTester() {

    @BeforeEach
    override fun setUp() {
        testDay = 20
        part1Solution = "19449262"
        part2Solution = "119"
        super.setUp()
        solver = Day20(inputText)
    }
}