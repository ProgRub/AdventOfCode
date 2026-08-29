import days.Day5
import org.junit.jupiter.api.BeforeEach

private class Day5Test : DayTester(){

    @BeforeEach
    override fun setUp() {
        testDay = 5
        part1Solution = "d4cd2ee1"
        part2Solution = "f2c730e5"
        super.setUp()
        solver = Day5(inputText)
    }
}