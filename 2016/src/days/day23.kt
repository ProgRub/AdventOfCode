package days

class Day23: IDaySolver{
    override var parser: IParser = Day23Parser()
    override var problemInput: Collection<String>

    constructor(input: List<String>) {
        this.problemInput = this.parser.parseTextToList(input[0],",")
    }

    override fun test() {
        TODO("Not yet implemented")
    }

    override fun part1() {
        TODO("Not yet implemented")
    }

    override fun part2() {
        TODO("Not yet implemented")
    }
}

class Day23Parser : IParser{

}