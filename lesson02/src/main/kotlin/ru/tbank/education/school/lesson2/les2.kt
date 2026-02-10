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

// Дополнительный конструктор - отдельная функция, которая позволяет создавать объект, указывая лишь некоторые параметры, остальные будут по умолчанию
class Apple6 {
    var color: String = "Red"
    var shape: String = "Round"
    var taste: String = "Sweet"
    var leaf: Boolean = true
    private var capacity: Int = 100

    constructor(color: String, shape: String, taste: String, leaf: Boolean, capacity: Int) {
        this.color = color
        this.shape = shape
        this.taste = taste
        this.leaf = leaf
        this.capacity = capacity
    }


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
fun p4() {
    val p52: Apple5 = Apple5(
        color = "purple",
        shape = "squared",
        leaf = false
    )
    // те мы получили яблоко
    // color = purple
    // shape = squared
    // taste = sweet
    // leaf = false
    // capacity = 100
}





// Наследование
// open - разрешает наследование и переопределение методов
// super - обращение к родительскому классу
open class Food {
    protected var cap: Int = 100

    open fun eat(bite: Int = 10): Int {
        if (cap > bite) {
            cap -= bite
            print("Successfully bitten")
        } else {
            print("Not enough capacity")
        }
        return cap
    }

    fun cap_left(): Int {
        return cap
    }
}

fun bite(food: Food) {
    food.eat()
}

// Дочерний класс Meat
class Meat(
    val type: String,
    var cooked: Boolean
) : Food() {

    // Переопределяем метод eat (через override)
    override fun eat(bite: Int): Int {
        return if (cooked) {
            super.eat(bite)
        } else {
            print("Not cooked")
            cap
        }
    }
}

// Дочерний класс Apple
class Apple7(
    val shape: String
) : Food()

fun p5() {
    val apl7 = Apple7(shape = "round")
    val meat1 = Meat(type = "beef", cooked = true)
    val meat2 = Meat(type = "beef", cooked = false)

    bite(apl7)
    bite(meat1)  // Successfully bitten
    bite(meat2)  // Not cooked
}




// Вложенные и внутренние классы
class Meat2(val type: String, var cooked: Boolean = false) {
    // Вложенный класс (без доступа к свойствам внешнего класса): маринад
    class Marinade(val flavor: String) {
        fun info() = print("Marinade: $flavor")
    }
    // Внутренний класс (с доступом к свойствам внеш класса): соус
    inner class Sauce(val spiciness: Int) {
        fun info() {
            print("Sauce for $type, spiciness $spiciness, ready? $cooked")
        }
        fun cook() {
            cooked = true
        }
    }
}

fun p6() {
    val beef = Meat2("Beef")
    Meat2.Marinade("Garlic").info()
    val hot = beef.Sauce(5)
    hot.info()
    hot.cook()
    hot.info()
}




//enum классы - постоянные, не меняются, константы, можно обратиться в любой момент
enum class MarinadeType(val flavor: String) {
    GARLIC("Чесночный"),
    SPICY("Острый"),
    HERB("Травяной")
}
// MarinadeType.GARLIC -> "Чесночный"




//abstract классы/функции - не имеет реализации (например, еда)
// abstract class
// abstract fun

//data классы - запрещают наследования, используются для иммутабельных (=неизменяемых) классов
data class Meat3(
    val type: String,
    val marinade: String,
    val cooked: Boolean
)


fun main() {
    p1()
    print('\n')
    p2()
    print('\n')
    p5()
    print('\n')
    p6()
}