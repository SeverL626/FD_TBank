package ru.tbank.education.school.lesson1
import kotlin.math.*

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
            if (a >= 0.0) {
                return sqrt(a)
            } else {
                return null
            }
        }
    }
    return null
}

fun OperatorClassType(operator: String): OperationType? {
    if (operator == "+") return OperationType.ADD
    if (operator == "-") return OperationType.SUBTRACT
    if (operator == "*") return OperationType.MULTIPLY
    if (operator == "/") return OperationType.DIVIDE
    return null
}

@Suppress("ReturnCount")
fun String.calculate(): Double? {
    val x = this.trim().replace("(", "").replace(")", "").split(" ")
    if (x.size == 3) {
        val a = x[0].toDoubleOrNull()
        val b = x[2].toDoubleOrNull()
        val op = OperatorClassType(x[1])
        if (a == null || b == null || op == null) {
            return null
        } else {
            return calculate(a, b, op)
        }
    } else if (x.size == 2) {
        val a = x[1].toDoubleOrNull()
        if (a == null) {
            return null
        } else {
            return HardCalculate(x[0], a)
        }
    } else {
        return null
    }
}