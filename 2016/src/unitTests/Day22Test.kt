import days.Day22
import org.junit.jupiter.api.BeforeEach

private class Day22Test : DayTester() {

    @BeforeEach
    override fun setUp() {
        testDay = 22
        part1Solution = "960"
        part2Solution = "225"
        super.setUp()
        solver = Day22(inputText)
    }
}