import days.Day12
import org.junit.jupiter.api.BeforeEach

private class Day12Test : DayTester() {

    @BeforeEach
    override fun setUp() {
        testDay = 12
        part1Solution = "318077"
        part2Solution = "9227731"
        super.setUp()
        solver = Day12(inputText)
    }
}