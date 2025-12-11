package ru.tbank.education.school.lesson6

data class User(
    val name: String,
    val orders: List<Order>
)

data class Order(
    val id: Long,
    val product: String,
    val amount: Long,
    var isPaid: Boolean = false,
    var isDelivered: Boolean = false
)

// Задание 1 - выведите все заказы
fun task1() {
    val users = listOf(
        User(
            "Анна", listOf(
                Order(1, "Телефон", 10000),
                Order(2, "Чехол", 100)
            )
        ),
        User(
            "Борис", listOf(
                Order(3, "Книга", 50),
                Order(4, "Рюкзак", 200)
            )
        )
    )

    val orders = users.flatMap{it.orders}
    println(orders)
}


// Задание 2 - выведите отчет в формате месяц-прибыль
fun task2() {
    val months = listOf("Янв", "Фев", "Мар", "Апр", "Май")
    val revenue = listOf(1000, 1200, 800, 1400, 1300)

    val report = months.zip(revenue)
    println(report)
}

// Задание 3 - выведите id всех заказов, которые были доставлены и оплачены на сумму > 1000
fun task3() {
    val orders = listOf(
        Order(id = 1, product = "Чехол", amount = 1200, isPaid = true, isDelivered = false),
        Order(id = 2, product = "Телефон", amount = 20000, isPaid = true, isDelivered = false),
        Order(id = 3, product = "Рюкзак", amount = 1000, isPaid = true, isDelivered = true),
        Order(id = 4, product = "Кружка", amount = 500, isPaid = false, isDelivered = false)
    )

    val ord_list = orders.filter{(it.isPaid) && (it.isDelivered) && (it.amount > 1000)}.map{it.id}
    println(ord_list)
}


data class Student(
    val name: String,
    val group: String
)


// Задание 4 - выведите каждую группу и студентов в ней
fun task4() {
    val students = listOf(
        Student(name = "Анна", group = "A-01"),
        Student(name = "Борис", group = "A-02"),
        Student(name = "Виктор", group = "A-01"),
        Student(name = "Галина", group = "A-03"),
        Student(name = "Денис", group = "A-02")
    )

    val g = students.groupBy{it.group}
    println(g)
}

data class ApiResponse(val code: Int, val message: String)

// Задание 5 - получить первый успешный ответ и последний ответ с ошибкой на стороне сервера
fun task5() {
    val responses = listOf(
        ApiResponse(code = 500, message = "Internal Error"),
        ApiResponse(code = 404, message = "Not Found"),
        ApiResponse(code = 200, message = "OK"),
        ApiResponse(code = 200, message = "Cached OK")
    )

    val firstSuccess = responses.firstOrNull{it.code == 200 }
    val lastServerError = responses.lastOrNull{it.code == 500}
    println("Первый успешный ответ: $firstSuccess")
    println("Последний ответ с ошибкой сервера: $lastServerError")
}

data class Movie(val title: String, val rating: Double)

// Задание 6 - получить топ 3 фильма по рейтингу
fun task6() {
    val movies = listOf(
        Movie(title = "Интерстеллар", rating = 9.0),
        Movie(title = "Начало", rating = 8.8),
        Movie(title = "Дюна", rating = 8.2),
        Movie(title = "Тёмный рыцарь", rating = 9.1),
        Movie(title = "Мементо", rating = 8.5)
    )

    val tir3 = movies.sortedByDescending{it.rating}.take(3)
    println(tir3)
}

// Задание 7 - добавить логирование операций
fun task7() {
    val add: (Int, Int) -> Int = { a, b -> a + b }
    val subtract: (Int, Int) -> Int = { a, b -> a - b }
    val multiply: (Int, Int) -> Int = { a, b -> a * b }

    fun logCall(name: String, op: (Int, Int) -> Int, a: Int, b: Int): Int {
        println("Выполняется $name, аргументы: ($a, $b)")
        val result = op(a, b)
        println("Вычисление завершено. Результат: $result")
        return result
    }

    logCall("СЛОЖЕНИЕ", add, 5, 3)
    logCall("ВЫЧИТАНИЕ", subtract, 10, 4)
    logCall("УМНОЖЕНИЕ", multiply, 6, 7)
}


data class Client(
    val name: String,
    val email: String,
    val phone: String,
)


fun <A, B, C> ((A) -> B).andThen(next: (B) -> C): (A) -> C = { a -> next(this(a)) }

// Задание 8 - хочу написать функции для валидации полей
fun task8() {
    val rawClients = listOf(
        Client(name = "  Иван  ", email = "  IVAN@MAIL.RU  ", phone = " +7 (999) 123-45-67 "),
        Client(name = "  Мария  ", email = "maria@mail.ru", phone = "8-800-555-35-35"),
        Client(name = " ", email = "test@", phone = "000"),
    )

    fun t8_name(n: String): String {
        val nn: String = n.trim().lowercase().replaceFirstChar{it.uppercaseChar()}
        return nn
    }

    fun t8_email(n: String): String {
        val nn = n.trim().lowercase()
        val parts = nn.split("@")
        if (parts.size != 2) return ""
        if (!parts[1].contains(".")) return ""
        if (parts[0].isBlank() || parts[1].isBlank()) return ""
        return nn
    }

    fun t8_phone(n: String): String {
        var d = n.trim().filter { it.isDigit() }
        return d
    }

    val cleanClients = rawClients.map { client ->
        client.copy(
            name = t8_name(client.name),
            email = t8_email(client.email),
            phone = t8_phone(client.phone)
        )
    }


    println(cleanClients)
}

// Задание 9 - просто смотрим на примеры
fun task9() {
    val student = "Коля" to 14  // Создаем пару (Имя, Возраст)
    val subject = "Математика" to 5  // Создаем пару (Предмет, Оценка)

    println("Ученик: ${student.first}, возраст: ${student.second}")
    println("Предмет: ${subject.first}, оценка: ${subject.second}")



    print("Обратный отсчёт: ")
    for (i in 5 downTo 1) {
        print("$i..")
    }


    print("Чётные числа от 2 до 10: ")
    for (num in 2..10 step 2) {
        print("$num ")
    }

    val fruits = listOf("яблоко", "банан", "апельсин", "груша")
    print("Первые 3 фрукта: ")
    for (i in 0 until 3) {  // Только 0, 1, 2 (без последнего)
        print("${fruits[i]} ")
    }
}

// Задание 10 - напишите функцию деления с использованием runCatching и Result<T>, реализуйте вывод ошибки и реузльтата
fun task10() {
}

// Задание 11 - пример
fun task11() {
//    val sayHello = { println("Hello") }
//    sayHello()

    val sayHello: Function0<*> = object : Function0<Any?> {
        override fun invoke(): Any {
            println("Hello")
            return Unit
        }
    }
    sayHello.invoke()
}

val lambda = object : Function0<Unit> {
    override fun invoke() {
        println("Hello")
    }
}

val lambdaFun = { println("Hello") }

// 0 параметров
interface Function0<out R> {
    fun invoke(): R
}

// 1 параметр
interface Function1<in P1, out R> {
    fun invoke(p1: P1): R
}

// 2 параметра
interface Function2<in P1, in P2, out R> {
    fun invoke(p1: P1, p2: P2): R
}


fun main() {
    task1()
    task2()
    task3()
    task4()
    task5()
    task6()
    task7()
    task8()
    task9()
    task10()
    task11()
}