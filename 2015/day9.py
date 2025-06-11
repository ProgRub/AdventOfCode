import sys

# setting path
sys.path.append("..\\AdventOfCode")

# importing
import common
from dataclasses import dataclass


@dataclass
class Connection:
    start: str
    end: str
    distance: int

    def __init__(self, start: str = "", end: str = "", distance: int = 0):
        self.start = start
        self.end = end
        self.distance = distance

    # Determines connection from the input
    @classmethod
    def fromLine(cls, line: str):
        distance = int(line.split(" = ")[1])
        split = line.split(" to ")
        start = split[0].strip()
        end = split[1].split(" = ")[0]
        return cls(start, end, distance)

    # Determines connection from its parameters
    @classmethod
    def fromConnection(cls, start: str, end: str, distance: int):
        return cls(start, end, distance)


@dataclass
class Course:
    locations: list[str]
    totalDistance: int

    def __init__(self):
        self.locations = []
        self.totalDistance = 0

    # Adds location to course and updates the total distance based on the connection to make
    def addLocation(self, connection: Connection):
        if self.locations == []:
            self.locations += [connection.start]
        self.locations += [connection.end]
        self.totalDistance += connection.distance

    # Determines if he can make this connection based on the current course
    # It can only go to each location ONCE
    def canTravel(self, connection: Connection):
        return connection.start == self.locations[-1] and connection.end not in self.locations

    def __str__(self):
        return f"{' -> '.join(self.locations)}: {self.totalDistance}"


# This function plots the best course (shortest or longest) starting with the first connection
def plotBestCourse(
    firstConnection: Connection,
    connections: list[Connection],
    courseLength: int, # param that determines what length the course needs to be (needs to visit every location, but only once)
    lookForShortestCourse: bool, #param that determines if we want the shortest or longest course possible
) -> Course:
    course = Course()
    course.addLocation(firstConnection)
    startingPoint = course.locations[-1] 
    # Need to make a copy because remove functions is in-place
    copy = connections.copy()
    copy.remove(firstConnection)
    # Sort the list by the distance of the connection, and if we're looking for the shortest course then it's ascending (not reversed)
    # If we're looking for the longest then it's descending order (reversed)
    copy.sort(key=lambda x: x.distance, reverse=not lookForShortestCourse)
    # Needs to visit every location once
    while len(course.locations) < courseLength:
        # Filter the sorted list of connections by the ones we can actually make, travelling from the location we're currently stopped at
        filteredList = list(
            filter(lambda x: x.start == startingPoint and course.canTravel(x), copy)
        )
        try:
            course.addLocation(filteredList[0]) # Since the list is ordered we know the first element is the best possible connection
            startingPoint = course.locations[-1] # Update the starting point for next cycle
        except IndexError:
            # If there was an index error then there wasn't a possible connection, we leave the loop
            break
    return course


def part1():
    inputValues = common.getInput()
    courses = getAllCourses(inputValues, True)
    return min([x.totalDistance for x in courses])


def getAllCourses(inputValues, lookForShortestCourse: bool):
    connections = [Connection.fromLine(x) for x in inputValues]
    copy = connections.copy()
    # Since he can make the travel in both ways, we travel the initial list of connections and reverse them
    for conn in copy:
        connections += [Connection.fromConnection(conn.end, conn.start, conn.distance)]
    distinctLocations = len(set([x.start for x in connections]))
    courses = []
    for conn in connections:
        course = plotBestCourse(
            conn, connections, distinctLocations, lookForShortestCourse
        )
        # Only add to the list of courses if it's valid (it needs to visit each location, but only once)
        if len(course.locations) == distinctLocations:
            courses += [course]
    return courses


def part2():
    inputValues = common.getInput()
    courses = getAllCourses(inputValues, False)
    return max([x.totalDistance for x in courses])


def main():
    print(part2())


if __name__ == "__main__":
    main()
