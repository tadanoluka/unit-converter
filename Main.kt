package converter

import kotlin.math.pow

// Possible unit type: "Weight", "Length", "Temperature"
enum class Units(val possibleValues: List<String>, val type: String, val canBeNegative: Boolean, val multiplayer: Double = 1.0, val summand: Double = 0.0) {
    GRAM(listOf("g", "gram", "grams"), "Weight", false),
    KILOGRAM(listOf("kg", "kilogram", "kilograms"), "Weight", false, 1000.0),
    MILLIGRAM(listOf("mg", "milligram", "milligrams"), "Weight", false, 0.001),
    POUND(listOf("lb", "pound", "pounds"), "Weight", false, 453.592),
    OUNCE(listOf("oz", "ounce", "ounces"), "Weight", false, 28.3495),
    METER(listOf("m", "meter", "meters"), "Length", false),
    KILOMETER(listOf("km", "kilometer", "kilometers"), "Length", false, 1000.0),
    CENTIMETER(listOf("cm", "centimeter", "centimeters"), "Length", false, 0.01),
    MILLIMETER(listOf("mm", "millimeter", "millimeters"), "Length", false, 0.001),
    MILE(listOf("mi", "mile", "miles"), "Length", false, 1609.35),
    YARD(listOf("yd", "yard", "yards"), "Length", false, 0.9144),
    FOOT(listOf("ft", "foot", "feet"), "Length", false, 0.3048),
    INCH(listOf("in", "inch", "inches"), "Length", false, 0.0254),
    CELSIUS(listOf("dc", "degree Celsius", "degrees Celsius", "celsius", "c"), "Temperature", true),
    FAHRENHEIT(listOf("df", "degree Fahrenheit", "degrees Fahrenheit", "fahrenheit", "f"), "Temperature", true, 5.0 / 9.0, 32.0),
    KELVIN(listOf("k", "kelvin", "kelvins"), "Temperature", true, 1.0, 273.15),
    NULL(listOf(),"", false);

    fun getPlural() = possibleValues[2]
    fun getSingular() = possibleValues[1]
}

fun isStringNumber(string: String) = string.toDoubleOrNull() != null

fun parseInput(inputString: String): MutableList<String> {
    val inputValue: String
    val inputUnit: String
    val outputUnit: String
    val inputStringList = inputString.split(" ")
    return if (inputStringList.size in 4..6 && isStringNumber(inputStringList[0])) {
        when (inputStringList.size) {
            4 -> {
                inputValue = inputStringList[0]
                inputUnit = inputStringList[1].lowercase()
                outputUnit = inputStringList[3].lowercase()
                mutableListOf(inputValue, inputUnit, outputUnit)
            }
            6 -> {
                inputValue = inputStringList[0]
                inputUnit = "${inputStringList[1].lowercase()} ${inputStringList[2].lowercase()}"
                outputUnit = "${inputStringList[4].lowercase()} ${inputStringList[5].lowercase()}"
                mutableListOf(inputValue, inputUnit, outputUnit)
            }
            else -> {
                if (inputStringList[1].lowercase() == "degree" || inputStringList[1].lowercase() == "degrees") {
                    inputValue = inputStringList[0]
                    inputUnit = "${inputStringList[1].lowercase()} ${inputStringList[2].lowercase()}"
                    outputUnit = inputStringList[4].lowercase()
                    mutableListOf(inputValue, inputUnit, outputUnit)
                } else {
                    inputValue = inputStringList[0]
                    inputUnit = inputStringList[1].lowercase()
                    outputUnit = "${inputStringList[3].lowercase()} ${inputStringList[4].lowercase()}"
                    mutableListOf(inputValue, inputUnit, outputUnit)
                }
            }
        }
    } else mutableListOf("")
}

fun parseError() = println("Parse error")

fun findUnit(inputUnitString: String): Units {
    for (unit in Units.values()) {
        for (item in unit.possibleValues) {
            if (inputUnitString == item.lowercase()) return unit
        }
    }
    return Units.NULL
}

fun isConversionPossible(inputValue: Double, inputUnit: Units, targetUnit: Units): Boolean {
    return if (inputUnit != Units.NULL && targetUnit != Units.NULL) {
        // Checking whether the units of measurement are in the same measurement system
        if (inputUnit.type.lowercase() == targetUnit.type.lowercase()) {
            // Checking whether the value is not negative or can be negative
            if (inputValue > 0 || inputUnit.canBeNegative) {
                true
            } else {
                println("${inputUnit.type.replaceFirstChar { it.uppercase() }} shouldn't be negative")
                false
            }

        } else {
            println("Conversion from ${inputUnit.getPlural()} to ${targetUnit.getPlural()} is impossible")
            false
        }
    } else {
        println("Conversion from ${if (inputUnit != Units.NULL) inputUnit.getPlural() else "???"} to " +
                "${if (targetUnit != Units.NULL) targetUnit.getPlural() else "???"} is impossible")
        false
    }
}

fun weightConversion(inputValue: Double, inputUnit: Units, targetUnit: Units) {
    val targetValue: Double = inputValue * (inputUnit.multiplayer / targetUnit.multiplayer)
    println("$inputValue ${if (inputValue == 1.0) inputUnit.getSingular() else inputUnit.getPlural()} is " +
            "$targetValue ${if (targetValue == 1.0) targetUnit.getSingular() else targetUnit.getPlural()}")
}
fun lengthConversion(inputValue: Double, inputUnit: Units, targetUnit: Units) {
    val targetValue: Double = inputValue * (inputUnit.multiplayer / targetUnit.multiplayer)
    println("$inputValue ${if (inputValue == 1.0) inputUnit.getSingular() else inputUnit.getPlural()} is " +
            "$targetValue ${if (targetValue == 1.0) targetUnit.getSingular() else targetUnit.getPlural()}")
}
fun temperatureConversion(inputValue: Double, inputUnit: Units, targetUnit: Units) {
    val celsiusValue: Double = (inputValue - inputUnit.summand) * inputUnit.multiplayer
    val targetValue: Double = celsiusValue * targetUnit.multiplayer.pow(-1) + targetUnit.summand
    println("$inputValue ${if (inputValue == 1.0) inputUnit.getSingular() else inputUnit.getPlural()} is " +
            "$targetValue ${if (targetValue == 1.0) targetUnit.getSingular() else targetUnit.getPlural()}")
}

fun main() {
    while (true) {
        print("Enter what you want to convert (or exit): ")
        val inputString = readln()

        if (inputString == "exit") {
            break
        }
        // [0] = input value, [1] = input unit, [2] = target unit
        val parsedInputMutableList = parseInput(inputString)
        if (parsedInputMutableList.size > 1) {
            val inputValue = parsedInputMutableList[0].toDouble()
            val inputUnit = findUnit(parsedInputMutableList[1])
            val targetUnit = findUnit(parsedInputMutableList[2])
            if (isConversionPossible(inputValue, inputUnit, targetUnit)) {
                when (inputUnit.type.replaceFirstChar { it.uppercase() }) {
                    "Weight" -> weightConversion(inputValue, inputUnit, targetUnit)
                    "Length" -> lengthConversion(inputValue, inputUnit, targetUnit)
                    "Temperature" -> temperatureConversion(inputValue, inputUnit, targetUnit)
                    else -> println("Unknown type conversion error")
                }
            }
        } else parseError()
    }
}
