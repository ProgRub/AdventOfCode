import days.Day6
import org.junit.jupiter.api.BeforeEach

class Day6Test : DayTester(){

    @BeforeEach
    override fun setUp() {
        testDay = 6
        part1Solution = "cyxeoccr"
        part2Solution = "batwpask"
        super.setUp()
        solver = Day6(inputText)
    }
}