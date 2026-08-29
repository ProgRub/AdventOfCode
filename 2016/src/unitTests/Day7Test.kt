import days.Day7
import org.junit.jupiter.api.BeforeEach

private class Day7Test : DayTester(){

    @BeforeEach
    override fun setUp() {
        testDay = 7
        part1Solution = "115"
        part2Solution = "231"
        super.setUp()
        solver = Day7(inputText)
    }
}