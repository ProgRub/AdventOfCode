import days.Day17
import org.junit.jupiter.api.BeforeEach

private class Day17Test : DayTester() {

    @BeforeEach
    override fun setUp() {
        testDay = 17
        part1Solution = "DUDRLRRDDR"
        part2Solution = "788"
        super.setUp()
        solver = Day17(inputText)
    }
}