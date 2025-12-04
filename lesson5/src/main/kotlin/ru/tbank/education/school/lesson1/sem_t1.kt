package ru.tbank.education.school.lesson1

import java.lang.ProcessBuilder.Redirect.to

fun main() {
    // Исходный список продуктов
    val products = listOf("Молоко", "Хлеб", "Сахар", "Сыр", "Масло", "Колбаса", "Сметана", "Яблоки")

    // todo 1. Проверка наличия "Хлеб" в коллекции

    if (products.contains("Хлеб")) {
        print("Bread in. \n")
    } else {
        print("No bread. \n")
    }

    // todo 2. Сортировка по алфавиту и вывод

    print(products.sorted())
    print("\n")

    // todo 3.1 Вывод только продуктов, начинающихся на букву "С"

    for (i in products.indices) {
        if (products[i][0].lowercase() == "с") {
            print(products[i] + " ")
        }
    }
    print("\n")

    // todo 3.2

    print(products.filter{it[0].lowercase() == "с"})
    print("\n")

    // todo 3.3

    print(products.filter{it.startsWith("с", ignoreCase = true)})
    print("\n")
}