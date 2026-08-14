from common import common

MFCSAM_result = dict(
    children=3,
    cats=7,
    samoyeds=2,
    pomeranians=3,
    akitas=0,
    vizslas=0,
    goldfish=5,
    trees=3,
    cars=2,
    perfumes=1,
)


class AuntSue:
    id: int
    caracteristics: dict[str, int]

    def __init__(self, description: str):
        self.caracteristics = {}
        sueSeparator = description.find(":")
        self.id = int(description[:sueSeparator].split()[1])
        characteristics = description[sueSeparator + 1 :]
        for charact in characteristics.split(","):
            name = charact.split(":")[0].strip()
            value = int(charact.split(":")[1].strip())
            self.caracteristics[name] = value


# This function takes a list of aunties and compares it to the scan of the MFCSAM machine to find which one gave the gift
# Returns the number of the auntie who gave the gift
def findAuntieV1(aunties: list[AuntSue]) -> int:
    for aunt in aunties:
        foundHer = True
        for caract, value in aunt.caracteristics.items():
            # If any of the caracteristics I remember for the aunt doesn't match, move on to the next
            if MFCSAM_result[caract] != value:
                foundHer = False
                break
        if foundHer:
            return aunt.id
    return -1


# This function takes a list of aunties and compares it to the scan of the MFCSAM machine to find which one gave the gift
# However, for the cats and trees the aunt should have more than the MFCSAM result
# And for the pomeranians and goldfish she should have less
# Returns the number of the auntie who gave the gift
def findAuntieV2(aunties: list[AuntSue]) -> int:
    for aunt in aunties:
        foundHer = True
        for caract, value in aunt.caracteristics.items():
            # For cats and trees it needs to be lesser or equal, reject it if <=
            if caract in ("cats", "trees") and value <= MFCSAM_result[caract]:
                foundHer = False
                break
            # For pomeranians and goldfish it needs to be greater, reject it if >=
            if caract in ("pomeranians", "goldfish") and value >= MFCSAM_result[caract]:
                foundHer = False
                break
            # If any of the remaining caracteristics I remember for the aunt doesn't match, move on to the next
            if (
                caract not in ("cats", "trees", "pomeranians", "goldfish")
                and MFCSAM_result[caract] != value
            ):
                foundHer = False
                break
        if foundHer:
            return aunt.id
    return -1


def part1():
    inputValues = common.getInput()
    aunties = []
    for line in inputValues:
        aunties += [AuntSue(line)]
    return findAuntieV1(aunties)


def part2():
    inputValues = common.getInput()
    aunties = []
    for line in inputValues:
        aunties += [AuntSue(line)]
    return findAuntieV2(aunties)


def main():
    print(part2())


if __name__ == "__main__":
    main()
