import days.Day9
import org.junit.jupiter.api.BeforeEach

private class Day9Test : DayTester(){

    @BeforeEach
    override fun setUp() {
        testDay = 9
        part1Solution = "70186"
        part2Solution = "10915059201"
        super.setUp()
        solver = Day9(inputText)
    }
}