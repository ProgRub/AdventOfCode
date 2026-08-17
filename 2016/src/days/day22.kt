package days

class Day22: IDaySolver{
    override var parser: IParser = Day22Parser()
    override var problemInput: List<String>

    constructor(input: List<String>) {
        this.problemInput = this.parser.parseTextToList(input[0],",")
    }

    override fun test(): String {
        TODO("Not yet implemented")
    }

    override fun part1(): String {
        TODO("Not yet implemented")
    }

    override fun part2(): String {
        TODO("Not yet implemented")
    }
}

class Day22Parser : IParser{

}