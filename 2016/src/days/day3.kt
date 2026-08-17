package days

class Day3: IDaySolver{
    override var parser: IParser = Day3Parser()
    override var problemInput: List<String>

    constructor(input: List<String>) {
        this.problemInput = this.parser.parseTextToList(input[0],",")
    }

    override fun test(): Int {
        TODO("Not yet implemented")
    }

    override fun part1(): Int {
        TODO("Not yet implemented")
    }

    override fun part2(): Int {
        TODO("Not yet implemented")
    }
}

class Day3Parser : IParser{

}