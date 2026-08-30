import days.Day21
import org.junit.jupiter.api.BeforeEach

private class Day21Test : DayTester() {

    @BeforeEach
    override fun setUp() {
        testDay = 21
        part1Solution = "bgfacdeh"
        part2Solution = "bdgheacf"
        super.setUp()
        solver = Day21(inputText)
    }
}