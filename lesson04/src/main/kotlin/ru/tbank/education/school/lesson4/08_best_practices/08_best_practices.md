# Домашнее задание 8: Best Practices

## Задача 1: Информативные сообщения об ошибках

Дан плохой код:

```kotlin
fun transferMoney(from: Account, to: Account, amount: Double) {
    if (amount <= 0) throw Exception("Bad amount")
    if (from.balance < amount) throw Exception("Error")
    // ...
}
```

**Задание**:

- Перепишите код с информативными сообщениями
- Используйте require/check вместо if + throw
- Создайте специфичные типы исключений
- Добавьте контекстную информацию в сообщения

```kotlin
class InsufficientFundsException(
    val accountId: String,
    val requested: Double,
    val available: Double
) : Exception("Account $accountId has insufficient funds: requested $requested, available $available")

// Перепишите функцию
```

## Задача 2: Уровни обработки ошибок

Спроектируйте систему обработки ошибок для трехуровневого приложения:

```kotlin
// 1. Data Layer - работа с БД
class UserRepository {
    fun findUser(id: Int): User {
        // Может быть SQLException
    }
}

// 2. Business Layer - бизнес-логика
class UserService {
    fun getUser(id: Int): User {
        // Использует repository
    }
}

// 3. Presentation Layer - API endpoint
class UserController {
    fun handleGetUser(id: String): Response {
        // Использует service
    }
}
```

**Задание**:

- Определите, на каком уровне какие ошибки обрабатывать
- Создайте исключения для каждого уровня
- Реализуйте преобразование исключений между уровнями
- Покажите пример end-to-end обработки ошибки

## Задача 3: Railway Oriented Programming

Реализуйте обработку формы регистрации:

```kotlin
data class RegistrationForm(
    val username: String,
    val email: String,
    val password: String,
    val age: String
)

data class ValidatedUser(
    val username: String,
    val email: String,
    val password: String,
    val age: Int
)
```

**Задание**:

- Создайте цепочку валидации через Result
- Каждая проверка возвращает Result
- Соберите все ошибки валидации
- Используйте mapCatching для преобразований

```kotlin
fun validateUsername(username: String): Result<String> = runCatching {
    // Валидация
}

fun validateEmail(email: String): Result<String> = runCatching {
    // Валидация
}

fun validatePassword(password: String): Result<String> = runCatching {
    // Валидация
}

fun validateAge(age: String): Result<Int> = runCatching {
    // Валидация
}

fun validateForm(form: RegistrationForm): Result<ValidatedUser> {
    // Объедините все валидации
}
```
