package ru.tbank.education.school.lesson2
import kotlin.String

// Класс с неизменяемыми переменными, те все яблоки красные, круглые, сладкие, с листочком и на 100% целые
class Apple1 {
    val color: String = "Red"
    val shape: String = "Round"
    val taste: String = "Sweet"
    val leaf: Boolean = true
    val capacity: Int = 100
}

// Класс, аналогичный Яблоку1, но здесь можно менять размер и наличие листочка
class Apple2 {
    val color: String = "Red"
    val shape: String = "Round"
    val taste: String = "Sweet"
    var leaf: Boolean = true
    var capacity: Int = 100
}

// Изменение и чтение (под капотом - getter и setter)
fun p1() {
    var apl: Apple2 = Apple2()

    print(apl.capacity)

    apl.capacity = 77
    apl.leaf = false

    print(apl.capacity)
    print(apl.leaf)
}



// Модификатор доступа (запрет на чтение и изменение):
// private - нет доступа из вне
// protected - недоступно из вне, но доступно для дочерних классов
// internal - доступно только внутри пакета
// public
class Apple3 {
    val color: String = "Red"
    val shape: String = "Round"
    val taste: String = "Sweet"
    var leaf: Boolean = true
    private var capacity: Int = 100
}
// var apl3: Apple3 = Apple3()
// print(apl3.capacity)
// Kotlin: Cannot access 'capacity': it is private in 'Apple3'


// Для взаимодействия с private нужно использовать функции
class Apple4 {
    val color: String = "Red"
    val shape: String = "Round"
    val taste: String = "Sweet"
    var leaf: Boolean = true
    private var capacity: Int = 100

    fun eat(bite: Int = 10): Int {
        if (capacity > bite) {
            capacity -= bite
            print("Successfully bitten")
        } else {
            print("Not enough capacity")
        }
        return 0
    }

    fun apple_capacity(): Int {
        return capacity
    }
}
fun p2() {
    var apl4: Apple4 = Apple4()
    print(apl4.apple_capacity())
    apl4.eat(90)
    print(apl4.apple_capacity())
    apl4.eat(90)
}



// Конструктор - для этого класс указываем в ()
class Apple5(
    val color: String = "Red",
    val shape: String = "Round",
    val taste: String = "Sweet",
    var leaf: Boolean = true,
    private var capacity: Int = 100
) {
    fun eat(bite: Int = 10): Int {
        if (capacity > bite) {
            capacity -= bite
            print("Successfully bitten")
        } else {
            print("Not enough capacity")
        }
        return 0
    }

    fun apple_capacity(): Int {
        return capacity
    }
}

fun p3() {
    val p51 = Apple5(
        "Blue",
        "Squared",
        "Sweet",
        false,
        70
    )
}

//open - позволяет наследование от класса
//super - обращение к родительскому классу

//enum классы - постоянные, не меняются, константы, можно обратиться в любой момент

//abstract классы - не имеет реализации (например, еда)

//data классы - запрещают наследования, используются для иммутабельных (=неизменяемых) классов

fun main() {
    p1()
    p2()
    p3()
}