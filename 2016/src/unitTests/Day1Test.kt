import days.Day1
import org.junit.jupiter.api.BeforeEach

private class Day1Test: DayTester() {

    @BeforeEach
    override fun setUp() {
        testDay = 1
        part1Solution = "243"
        part2Solution = "142"
        super.setUp()
        solver = Day1(inputText)
    }
}