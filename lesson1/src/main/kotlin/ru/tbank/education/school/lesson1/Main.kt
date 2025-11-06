package ru.tbank.education.school.lesson1

fun main() {
    print("Hello World! V1 \n")
    println("Hello World! V2")

    // val - константа (нельзя изменить)
    val a = 1
    val b = "123" // строка ("asd")
    val c = 'c' // char ('x')
    val d = 1.25
    val e = true // true && false
    val f = """
        типо 
        на
        несколько
        строк
    """.trimIndent()

    val g = "hello" + d + "world"
    // аналогично
    val h = "hello $d world"

    // var - можно изменить, в остальном аналогично val

    // можно указать тип данных
    val i: Short = 2

    // ? <=> тип данных может быть null
    val j: Short? = null
    val k: Short? = 3



    // if
    if (j != null) {
        // тело
    } else {
        // тело
    }

    // или так
    var l = if (j != null) "it isn't null" else "it's null"

    var m = when (j != null) {
        true -> "it's true"
        false -> "it's false"
        else -> "it's unknown" // можно добавить, но невозможно дойти будет
    }



    // массивы (редко юзают, чаще - списки)

    var arr = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    print(arr[2])

    // двумерные массивы

    // С помощью arrayOf()
    val twoDimArray = arrayOf(
        arrayOf(1, 2, 3),
        arrayOf(4, 5, 6)
    )

    // Создание с помощью Array(); upd: {0} <=> заполняем все 0
    val anotherArray = Array(3) { Array(4) { 0 } }

    // filter
    val arf1 = arr.filter{it % 2 == 0}

    // можно агрегировать
    val arf2 = arr.filter{it % 2 == 0}.sum()



    // циклы (step - шаг, всегда положительный, не обязательно указывать)
    for (i in 1..10 step 1) {
        // тело
    }
    for (i in 10 downTo 6 step 2) {
        // тело
    }
    // проход по массиву
    for (elem in arr) {
        // тело
    }
}