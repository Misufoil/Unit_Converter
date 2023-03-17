enum class UnitType {LENGTH, WEIGHT, TEMPERATURE, UNKNOWN}

enum class Unit(val type: UnitType, val normalizedName: String, val factor: Double) {
    METER(UnitType.LENGTH, "meter", 1.0),
    KILOMETER(UnitType.LENGTH, "kilometer", 1_000.0),
    CENTIMETER(UnitType.LENGTH, "centimeter", 0.01),
    MILLIMETER(UnitType.LENGTH, "millimeter", 0.001),
    MILE(UnitType.LENGTH, "mile", 1_609.35),
    YARD(UnitType.LENGTH, "yard", 0.9144),
    FOOT(UnitType.LENGTH, "foot", 0.3048),
    INCH(UnitType.LENGTH, "inch", 0.0254),

    GRAM(UnitType.WEIGHT, "gram", 1.0),
    KILOGRAM(UnitType.WEIGHT, "kilogram", 1_000.0),
    MILLIGRAM(UnitType.WEIGHT, "milligram", 0.001),
    POUND(UnitType.WEIGHT, "pound", 453.592),
    OUNCE(UnitType.WEIGHT, "ounce", 28.3495),

    CELSIUS(UnitType.TEMPERATURE, "degree Celsius", 0.0),
    KELVIN(UnitType.TEMPERATURE, "kelvin", 0.0),
    FAHRENHEIT(UnitType.TEMPERATURE, "degree Fahrenheit", 0.0),

    UNKNOWN(UnitType.UNKNOWN, "???", 0.0);
}

fun main() {
    while (true) {
        print("Enter what you want to convert (or exit): ")
        val str = readln().replace("(?i)degrees?\\s*".toRegex(), "").lowercase()
        if (str == "exit") {
            break
        }
        try {
            val (value, unit, x, toUnit) = str.split(" ")
            val formattingUnit = formatting(unit)
            val formattingToUnit = formatting(toUnit)

            convertUnits(formattingUnit, formattingToUnit, value.toDouble())
            println()
        } catch (e: Exception) {
            println("Parse error")
            println()
            continue
        }
    }
}

fun convertUnits(formattingUnit: String, formattingToUnit: String, value: Double) {
    var fromVal = Unit.UNKNOWN
    var toVal = Unit.UNKNOWN
    var answer = value

    for (i in Unit.values()) {
        if (formattingUnit == i.normalizedName) {
            fromVal = i
        }
        if (formattingToUnit == i.normalizedName) {
            toVal = i
        }
    }

    if (check(fromVal, toVal, formattingUnit, formattingToUnit, value)) {
        return
    }

    if (fromVal.type == UnitType.TEMPERATURE) {
        if (fromVal == Unit.CELSIUS) {
            answer += 273.15
        } else if (fromVal == Unit.FAHRENHEIT) {
            answer = (answer + 459.67) * 5.0 / 9.0
        }

        if (toVal == Unit.CELSIUS) {
            answer -= 273.15
        } else if (toVal == Unit.FAHRENHEIT) {
            answer = (answer * 9.0 / 5.0 - 459.67)
        }
    } else {
        answer = value * fromVal.factor / toVal.factor
    }

    println("$value ${toPlural(value, formattingUnit)} is $answer ${toPlural(answer, formattingToUnit)}")
}

fun check(fromVal: Unit, toVal: Unit, formattingUnit: String, formattingToUnit: String, value: Double): Boolean {
    if (fromVal.type != UnitType.UNKNOWN && toVal.type != UnitType.UNKNOWN && fromVal.type == toVal.type) {
        if (value < 0) {
            if (fromVal.type == UnitType.LENGTH) {
                println("Length shouldn't be negative")
                return true
            }
            if (fromVal.type == UnitType.WEIGHT) {
                println("Weight shouldn't be negative")
                return true
            }
        }
        return false
    } else {
        println("Conversion from ${toPlural(2.0, formattingUnit)} to ${toPlural(2.0, formattingToUnit)} is impossible")
        return true
    }
}

fun formatting(value: String) = when (value) {
    "m", "meter", "meters" -> "meter"
    "km", "kilometer", "kilometers" -> "kilometer"
    "cm", "centimeter", "centimeters" -> "centimeter"
    "mm", "millimeter", "millimeters" -> "millimeter"
    "mi", "mile", "miles" -> "mile"
    "yd", "yard", "yards" -> "yard"
    "ft", "foot", "feet" -> "foot"
    "in", "inch", "inches" -> "inch"
    "g", "gram", "grams" -> "gram"
    "kg", "kilogram", "kilograms" -> "kilogram"
    "mg", "milligram", "milligrams" -> "milligram"
    "lb", "pound", "pounds" -> "pound"
    "oz", "ounce", "ounces" -> "ounce"
    "df", "fahrenheit", "f", "degree Fahrenheit", "degrees Fahrenheit" -> "degree Fahrenheit"
    "dc", "celsius", "c", "degree Celsius", "degrees Celsius" -> "degree Celsius"
    "kelvins", "kelvin", "k" -> "kelvin"
    else -> "???"
}

fun toPlural(value: Double, unit: String): String {
    return if (value != 1.0) {
        when (unit) {
            "foot" -> "feet"
            "inch" -> "inches"
            "degree Celsius" -> "degrees Celsius"
            "degree Fahrenheit" -> "degrees Fahrenheit"
            "???" -> "???"
            else -> unit + "s"
        }
    } else {
        unit
    }
}