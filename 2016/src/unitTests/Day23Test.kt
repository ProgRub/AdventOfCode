import days.Day23
import org.junit.jupiter.api.BeforeEach

private class Day23Test : DayTester() {

    @BeforeEach
    override fun setUp() {
        testDay = 23
        part1Solution = "12775"
        part2Solution = "479009335"
        super.setUp()
        solver = Day23(inputText)
    }
}