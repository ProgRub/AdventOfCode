import days.Day8
import org.junit.jupiter.api.BeforeEach

private class Day8Test : DayTester() {

    @BeforeEach
    override fun setUp() {
        testDay = 8
        part1Solution = "106"
        part2Solution = " ##  #### #    #### #     ##  #   #####  ##   ### \n" +
                "#  # #    #    #    #    #  # #   ##    #  # #    \n" +
                "#    ###  #    ###  #    #  #  # # ###  #    #    \n" +
                "#    #    #    #    #    #  #   #  #    #     ##  \n" +
                "#  # #    #    #    #    #  #   #  #    #  #    # \n" +
                " ##  #    #### #### ####  ##    #  #     ##  ###  \n"
        super.setUp()
        solver = Day8(inputText)
    }
}