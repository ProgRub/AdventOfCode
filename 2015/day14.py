from common import common
from enum import Enum


class States(Enum):
    FLYING = 0
    RESTING = 1


class Reindeer:
    flightSpeed: int  # in Kilometers per second
    maxFlightTime: int  # in seconds
    requiredRestingTime: int  # in seconds
    timeCounter: int  # in seconds
    name: str
    distanceCovered: int  # in Kilometers
    currentState: States

    def __init__(self, phrase: str):
        splitStr = phrase.split()
        self.name = splitStr[0]
        self.flightSpeed = int(splitStr[3])
        self.maxFlightTime = int(splitStr[6])
        self.requiredRestingTime = int(splitStr[13])
        self.distanceCovered = 0
        self.timeCounter = 0
        self.currentState = States.FLYING

    # After the reindeer is sufficiently rested, it can start flying again
    def startFlying(self):
        self.timeCounter = 0
        self.currentState = States.FLYING

    # The reindeer needs to rest when it hits the max flying time
    def stopFlying(self):
        self.timeCounter = 0
        self.currentState = States.RESTING

    def timePasses(self):
        self.timeCounter += 1
        match self.currentState:
            # If the reindeer is flying, add the distance covered in the time ticked and stop the flight if necessary
            case States.FLYING:
                self.distanceCovered += self.flightSpeed
                if self.timeCounter >= self.maxFlightTime:
                    self.stopFlying()
            # If the reindeer is resting there's no distance being covered, when it's done resting it can start flying again
            case States.RESTING:
                if self.timeCounter >= self.requiredRestingTime:
                    self.startFlying()

    def __str__(self) -> str:
        return f"{self.name}"


def part1():
    inputValues = common.getInput()
    reindeers: list[Reindeer] = []
    for phrase in inputValues:
        reindeers += [Reindeer(phrase)]
    time = 0
    while time < 2503:
        for reindeer in reindeers:
            reindeer.timePasses()
        time += 1
    # After the alloted time has passed check which reindeer flew the farthest
    maxDistanceCovered = 0
    for rein in reindeers:
        maxDistanceCovered = max(maxDistanceCovered, rein.distanceCovered)
    return maxDistanceCovered


def part2():
    inputValues = common.getInput()
    reindeers: list[Reindeer] = []
    points = []
    for phrase in inputValues:
        reindeers += [Reindeer(phrase)]
        points += [0]
    time = 0
    leaderDistanceCovered = 0
    while time < 2503:
        leaderIndices = []
        for index, reindeer in enumerate(reindeers):
            reindeer.timePasses()
            # If the current reindeer has covered more distance than the leader, update who the leader is
            if reindeer.distanceCovered > leaderDistanceCovered:
                leaderDistanceCovered = reindeer.distanceCovered
                leaderIndices = [
                    index
                ]  # If this reindeer has overtaken the previous leader then it's the only one that should get a point
            # If there's a tie for the lead this reindeer should also get a point
            elif reindeer.distanceCovered == leaderDistanceCovered:
                leaderIndices += [index]

        time += 1
        # Give a point to the reindeers leading
        for index in leaderIndices:
            points[index] += 1

    return max(points)


def main():
    print(part2())


if __name__ == "__main__":
    main()
