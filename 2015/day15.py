from common import common

totalTeaspoons = 100


class Ingredient:
    capacity: int
    durability: int
    flavour: int
    texture: int
    calories: int
    name: str

    def __init__(self, phrase: str):
        splitProperties = phrase[phrase.find(":") + 1 :].split(", ")
        self.name = phrase.split(": ")[0]
        for propText in splitProperties:
            property = propText.split()[0].strip()
            value = int(propText.split()[1])
            match property:
                case "capacity":
                    self.capacity = value
                case "durability":
                    self.durability = value
                case "flavor":
                    self.flavour = value
                case "texture":
                    self.texture = value
                case "calories":
                    self.calories = value

# Based on a solution off reddit
def calculateOptimalScore(ingredients: list[Ingredient], forPart2: bool) -> int:
    score = 0
    best = 0
    for i in range(0, totalTeaspoons):
        for j in range(0, totalTeaspoons - i):
            for k in range(0, totalTeaspoons - i - j):
                h = totalTeaspoons - i - j - k
                capacity = (
                    ingredients[0].capacity * i
                    + ingredients[1].capacity * j
                    + ingredients[2].capacity * k
                    + ingredients[3].capacity * h
                )
                durability = (
                    ingredients[0].durability * i
                    + ingredients[1].durability * j
                    + ingredients[2].durability * k
                    + ingredients[3].durability * h
                )
                flavour = (
                    ingredients[0].flavour * i
                    + ingredients[1].flavour * j
                    + ingredients[2].flavour * k
                    + ingredients[3].flavour * h
                )
                texture = (
                    ingredients[0].texture * i
                    + ingredients[1].texture * j
                    + ingredients[2].texture * k
                    + ingredients[3].texture * h
                )
                calories = (
                    ingredients[0].calories * i
                    + ingredients[1].calories * j
                    + ingredients[2].calories * k
                    + ingredients[3].calories * h
                )

                # Part 2
                if forPart2 and calories != 500:
                    continue
                if capacity <= 0 or durability <= 0 or flavour <= 0 or texture <= 0:
                    score = 0
                    continue
                score = capacity * durability * flavour * texture
                best = max(best, score)
    return best


def part1():
    inputValues = common.getInput()
    ingredients = []
    for phrase in inputValues:
        ingredients += [Ingredient(phrase)]
    return calculateOptimalScore(ingredients, False)


def part2():
    inputValues = common.getInput()
    ingredients = []
    for phrase in inputValues:
        ingredients += [Ingredient(phrase)]
    return calculateOptimalScore(ingredients, True)


def main():
    print(part2())


if __name__ == "__main__":
    main()
