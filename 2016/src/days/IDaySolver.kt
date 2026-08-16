package days

interface IDaySolver {
    var parser: IParser
    var problemInput : Collection<String>
    fun test()
    fun part1()
    fun part2()
}