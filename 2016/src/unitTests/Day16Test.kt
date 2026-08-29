import days.Day16
import org.junit.jupiter.api.BeforeEach

private class Day16Test : DayTester() {

    @BeforeEach
    override fun setUp() {
        testDay = 16
        part1Solution = "10011010010010010"
        part2Solution = "10101011110100011"
        super.setUp()
        solver = Day16(inputText)
    }
}