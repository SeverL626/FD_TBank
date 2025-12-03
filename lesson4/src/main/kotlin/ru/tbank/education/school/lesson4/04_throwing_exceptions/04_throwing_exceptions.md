# Домашнее задание 4: Выброс исключений

## Задача 1: Валидация параметров

Напишите функцию регистрации пользователя:

```kotlin
fun registerUser(username: String, email: String, age: Int): User {
    // Валидация:
    // 1. username не пустой и длиной от 3 до 20 символов
    // 2. email содержит @
    // 3. age от 18 до 120
    //
    // При невалидных данных выбрасывайте IllegalArgumentException с понятным сообщением
}
```

**Требования**:

- Проверяйте каждый параметр
- Сообщения об ошибках должны быть информативными
- Используйте require для валидации

## Задача 2: Переброс исключений

Дана функция низкого уровня:

```kotlin
fun readFromDatabase(query: String): String {
    // Симуляция работы с БД
    if (query.isEmpty()) throw SQLException("Empty query")
    return "result"
}
```

**Задание**:

- Напишите функцию высокого уровня `getUserData(userId: Int)`, которая:
    - Вызывает readFromDatabase
    - Ловит SQLException
    - Выбрасывает более понятное исключение UserNotFoundException
    - Сохраняет исходную ошибку как cause

```kotlin
class UserNotFoundException(message: String, cause: Throwable) : Exception(message, cause)

fun getUserData(userId: Int): UserData {
    // Ваш код
}
```

## Задача 3: Когда НЕ нужно выбрасывать исключения

Для каждой функции решите, нужно ли выбрасывать исключение или лучше вернуть специальное значение:

```kotlin
// A. Поиск пользователя по ID
fun findUser(id: Int): User?

// B. Деление чисел
fun divide(a: Double, b: Double): Double

// C. Парсинг JSON
fun parseJson(json: String): JsonObject
```

**Задание**:

- Для каждой функции обоснуйте выбор (исключение vs null vs специальное значение)
- Реализуйте каждую функцию согласно вашему решению
- Напишите пример использования каждой функции
