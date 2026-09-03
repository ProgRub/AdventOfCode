import days.Day10
import org.junit.jupiter.api.BeforeEach

private class Day10Test : DayTester() {

    @BeforeEach
    override fun setUp() {
        testDay = 10
        part1Solution = "116"
        part2Solution = "23903"
        super.setUp()
        solver = Day10(inputText)
    }
}