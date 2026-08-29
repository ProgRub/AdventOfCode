import days.Day4
import org.junit.jupiter.api.BeforeEach

private class Day4Test : DayTester(){

    @BeforeEach
    override fun setUp() {
        testDay = 4
        part1Solution = "361724"
        part2Solution = "482"
        super.setUp()
        solver = Day4(inputText)
    }
}