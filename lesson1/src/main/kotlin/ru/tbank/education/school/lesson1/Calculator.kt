package ru.tbank.education.school.lesson1
import kotlin.math.*

enum class OperationType {
    ADD, SUBTRACT, MULTIPLY, DIVIDE
}

fun calculate(a: Double, b: Double, operation: OperationType = OperationType.ADD): Double? {
    when (operation) {
        OperationType.ADD -> return a + b
        OperationType.SUBTRACT -> return a - b
        OperationType.MULTIPLY -> return a * b
        OperationType.DIVIDE -> {
            if (b != 0.0) {
                return a / b
            } else {
                return null
            }
        }
    }
}

fun HardCalculate(operation: String, a: Double): Double? {
    when (operation) {
        "sin" -> return sin(a)
        "cos" -> return cos(a)
        "sqrt" -> {
            if (a > 0.0) {
                return sqrt(a)
            } else {
                return null
            }
        }
    }
}

fun OperatorClassType()

@Suppress("ReturnCount")
fun String.calculate(): Double? {
    val x = this.trim().replace("(", "").replace(")", "").split(" ")
    if (x.size == 3) {
        val calc_flag = true
    } else if (x.size == 2) {
        val sqrt_flag = true
    } else {
        return null
    }






    val a = x[0].toDoubleOrNull()
    val b = x[2].toDoubleOrNull()
    if (a == null || b == null) {
        return null
    }

    val op: OperationType =
        if (x[1] == "+") {
        OperationType.ADD
    } else if (x[1] == "-") {
        OperationType.SUBTRACT
    } else if (x[1] == "*") {
        OperationType.MULTIPLY
    } else if (x[1] == "/") {
        OperationType.DIVIDE
    } else {
        return null
    }
    return calculate(a, b, op)
}