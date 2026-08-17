import days.Day2
import org.junit.jupiter.api.BeforeEach

class Day2Test :DayTester(){

    @BeforeEach
    override fun setUp() {
        testDay = 2
        part1Solution = "19636"
        part2Solution = "3CC43"
        super.setUp()
        solver = Day2(inputText)
    }
}