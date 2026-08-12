from common import common

totalNumberPeople = 8
maxPossibleHappinessConnection = 100


class Connection:
    person1: str
    person2: str
    happyGain: int

    def __init__(self, phrase: str):
        splitStr = phrase.split()
        self.person1 = splitStr[0]
        # Strip period if phrase ends with it
        self.person2 = splitStr[-1].strip(".")
        # Determine if person1 will gain or lose happiness by sitting next to the person
        # (gain -> positive multiplier; lose -> negative multiplier)
        multiplier = 1 if splitStr[2] == "gain" else -1
        self.happyGain = multiplier * int(splitStr[3])

    def __str__(self) -> str:
        return f"{self.person1} {self.happyGain} {self.person2}"


class BestTableCalculator:
    people: list[str]
    allConnections: list[Connection]
    firstPersonSeated: str
    happinessCutoff: int

    def __init__(self, phrases: list[str]):
        self.seats = []
        self.allConnections = self.parseConnections(phrases)
        # get the distinct list of all people to know who still needs to be seated
        self.people = list(set([conn.person1 for conn in self.allConnections]))
        self.people.sort()  # sorted list since the connections are sorted
        self.happinessCutoff = 0

    # This function is responsible for parsing the phrases into a usable list of connections between 2 people
    def parseConnections(self, phrases: list[str]) -> list[Connection]:
        return [Connection(phrase) for phrase in phrases]

    # This function is responsible for finding the optimum roundtable for max happiness. Yupee!
    # It returns that optimum roundtable's happiness
    def findOptimumTableHappiness(self) -> int:
        self.happinessCutoff = -100000
        # Get all tables starting with each person
        # Recursively go through each table like a binary tree because each person can only appear once in the table
        for person in self.people:
            self.firstPersonSeated = person
            bestHappiness = self.getBestTableFromPerson(
                person, 0, self.allConnections, 0
            )
            self.happinessCutoff = max(self.happinessCutoff, bestHappiness)
        return self.happinessCutoff

    # This function is responsible for checking every roundtable starting from said person.
    # It also takes in the happiness cutoff so as to not waste time building a table that will not beat existing optimum roundtable
    def getBestTableFromPerson(
        self,
        personSeated: str,
        seatsFilled: int,
        connections: list[Connection],
        currentTableHappiness: int,
    ) -> int:
        # Number of seats filled is increased by one by seating the person to be seated
        seatsFilled += 1
        bestTableHappiness = -10000
        # The possible connections to make from the person being seated to the ones that still need seating (filtered in the call)
        seatedPersonConnections = [
            conn for conn in connections if conn.person1 == personSeated
        ]
        # When we reach the end of a branch it does not enter the for loop since there's no possible connections
        # But the table happiness is missing the connection between the first and last person of the loop
        if seatedPersonConnections == []:
            lastConnection = [
                conn
                for conn in self.allConnections
                if conn.person1 == personSeated
                and conn.person2 == self.firstPersonSeated
            ][0]
            currentTableHappiness += (
                lastConnection.happyGain
                + self.getReverseConnectionHappiness(
                    lastConnection, self.allConnections
                )
            )
            bestTableHappiness = max(bestTableHappiness, currentTableHappiness)
        # Check every possible roundtable from the remaining people
        for connection in seatedPersonConnections:
            # We need to sum to the current table happiness the current connection's value and the reverse connection's value
            # Because they are seated together
            tableHappinessUpdated = (
                currentTableHappiness
                + connection.happyGain
                + self.getReverseConnectionHappiness(connection, connections)
            )
            # If the calculated happiness summed with perfect connections for the seats that remain to be filled doesn't beat the happiness cutoff
            # Then there's no point to keep checking this table
            if (
                tableHappinessUpdated
                + (
                    maxPossibleHappinessConnection
                    * 2
                    * (totalNumberPeople - seatsFilled)
                )
                < self.happinessCutoff
            ):
                break
            # Pass connections without the connections ending in the seated person beacause the reverse happiness was already calculated
            possibleConnections = [
                conn for conn in connections if conn.person2 != personSeated
            ]
            # Continue building out table
            tableHappinessUpdated = self.getBestTableFromPerson(
                connection.person2,
                seatsFilled,
                possibleConnections,
                tableHappinessUpdated,
            )
            # Update the best table so far if the current table tops it
            bestTableHappiness = max(
                bestTableHappiness, tableHappinessUpdated, currentTableHappiness
            )
        return bestTableHappiness

    # Since the connections are two sided, to calculate happiness we need to take into account the happiness gain (or loss) for both people
    # This function takes a connection, and the list of connections to check, and returns the happiness of the reversed connection
    # I.e. Person 1 is person 2 and person 2 is person 1
    # Returns 0 if no reverse connection is found
    def getReverseConnectionHappiness(
        self, conn: Connection, connections: list[Connection]
    ) -> int:
        happy = 0
        for connection in connections:
            if (
                connection.person1 == conn.person2
                and connection.person2 == conn.person1
            ):
                happy = connection.happyGain
                break
        return happy


def part1():
    inputValues = common.getInput()
    calculator = BestTableCalculator(inputValues.tolist())
    return calculator.findOptimumTableHappiness()


def part2():
    # Input changed because that's the development for part2
    # To revert back to day 1 remove all lines containing 'Rúben'
    inputValues = common.getInput()
    calculator = BestTableCalculator(inputValues.tolist())
    return calculator.findOptimumTableHappiness()


def main():
    print(part2())


if __name__ == "__main__":
    main()
