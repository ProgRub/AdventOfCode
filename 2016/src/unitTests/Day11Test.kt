import days.Day11
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

private class Day11Test : DayTester() {

    @BeforeEach
    override fun setUp() {
        testDay = 11
        part1Solution = "33"
        part2Solution = "57"
        inputText = listOf()
        solver = Day11(inputText)
    }

    @Test
    @DisplayName("Part 1 test")
    override fun part1test() {
        val text = "The first floor contains a promethium generator and a promethium-compatible microchip.\n" +
                "The second floor contains a cobalt generator, a curium generator, a ruthenium generator, and a plutonium generator.\n" +
                "The third floor contains a cobalt-compatible microchip, a curium-compatible microchip, a ruthenium-compatible microchip, and a plutonium-compatible microchip.\n" +
                "The fourth floor contains nothing relevant."
        inputText = text.split("\n")
        solver = Day11(inputText)
        Assertions.assertEquals(part1Solution, solver.part1())
    }

    @Test
    @DisplayName("Part 2 test")
    override fun part2test() {
        val text = "The first floor contains a promethium generator, promethium-compatible microchip, elerium generator, elerium-compatible microchip, dilithium generator and a dilithium-compatible microchip.\n" +
                "The second floor contains a cobalt generator, a curium generator, a ruthenium generator, and a plutonium generator.\n" +
                "The third floor contains a cobalt-compatible microchip, a curium-compatible microchip, a ruthenium-compatible microchip, and a plutonium-compatible microchip.\n" +
                "The fourth floor contains nothing relevant."
        inputText = text.split("\n")
        solver = Day11(inputText)
        Assertions.assertEquals(part2Solution, solver.part2())
    }
}