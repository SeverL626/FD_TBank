package ru.tbank.education.school.lesson1

fun sumEvenNumbers(numbers: Array<Int>): Int {
    var sum = 0
    for (i in numbers) {
        if (i % 2 == 0) {
            sum += i
        }
    }
    return sum
}
// пример работы
fun main() {
    print(sumEvenNumbers(arrayOf(1, 2, 3, 4, 5)))
}
