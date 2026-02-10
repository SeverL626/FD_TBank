# Домашнее задание 6: Работа с Null

## Задача 1: Безопасная цепочка вызовов

Дана структура данных:

```kotlin
data class Address(val city: String, val street: String?)
data class Company(val name: String, val address: Address?)
data class User(val name: String, val company: Company?)
```

**Задание**:
Напишите функции:

```kotlin
// Вернуть название улицы или "Unknown"
fun getStreet(user: User?): String {
    // Используйте safe call и Elvis operator
}

// Вернуть город в uppercase или null
fun getCityUppercase(user: User?): String? {
    // Используйте цепочку safe calls и let
}

// Проверить, что у пользователя есть компания в Москве
fun isFromMoscow(user: User?): Boolean {
    // Ваш код
}
```

## Задача 2: Рефакторинг императивного кода

Дан императивный код:

```kotlin
fun processOrder(orderId: String): String {
    val order = findOrder(orderId)
    if (order == null) {
        return "Order not found"
    }

    val customer = order.customer
    if (customer == null) {
        return "Customer not found"
    }

    val email = customer.email
    if (email == null) {
        return "Email not found"
    }

    return "Sending email to: $email"
}
```

**Задание**:

- Перепишите код используя safe call, let и Elvis operator
- Код должен стать максимально коротким и читаемым
- Сохраните ту же логику

## Задача 3: Когда использовать !!

Проанализируйте код:

```kotlin
fun processUser(userId: String) {
    val user = findUser(userId)
    val name = user!!.name
    val age = user!!.age
    val email = user!!.email

    println("User: $name, $age, $email")
}
```

**Задание**:

- Что не так с этим кодом?
- Перепишите без использования !!
- Приведите пример ситуации, когда использование !! оправдано
- Напишите альтернативу с проверкой и early return
