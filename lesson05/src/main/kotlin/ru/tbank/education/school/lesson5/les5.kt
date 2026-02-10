package ru.tbank.education.school.lesson5

// ===========================
// Generics (дженерики)
// ===========================
// Дженерики — это обобщение кода для разных типов данных.
// Позволяют писать один класс/метод для разных типов, не жестко задавая их.

// ---------------------------
// Простой пример класса с generic
class Box<T>(val value: T) {
    fun get(): T = value
}

val intBox = Box(42)       // T = Int
val strBox = Box("hello")  // T = String

// ---------------------------
// Ограничения типа (type constraints)
// T может быть подтипом какого-то класса или интерфейса
class NumberBox<T: Number>(val value: T) {
    fun doubleValue(): Double = value.toDouble() * 2
}

// T должен быть Comparable
fun <T: Comparable<T>> maxOf(a: T, b: T): T = if (a > b) a else b

// ---------------------------
// Несколько ограничений через where
fun <T> sortAndPrint(list: List<T>)
        where T: Comparable<T>, T: Any {
    println(list.sorted())
}

// ---------------------------
// Кратко:
// T — любой тип
// T: SuperType — ограничение по типу
// where T: A, T: B — несколько ограничений


// Продвинутые коллекции (после ФП, лекция 6)

