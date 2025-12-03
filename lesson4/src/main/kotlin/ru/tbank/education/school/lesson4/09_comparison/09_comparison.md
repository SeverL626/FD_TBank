# Домашнее задание 9: Сравнение подходов

## Задача 1: Четыре способа обработки ошибок

Реализуйте функцию поиска пользователя четырьмя способами:

```kotlin
// Способ 1: Исключения
fun findUserException(id: Int): User {
    // Выбрасывает UserNotFoundException если не найден
}

// Способ 2: Nullable
fun findUserNullable(id: Int): User? {
    // Возвращает null если не найден
}

// Способ 3: Result
fun findUserResult(id: Int): Result<User> {
    // Возвращает Result
}

// Способ 4: Sealed class
sealed class UserResult {
    data class Success(val user: User) : UserResult()
    data class NotFound(val id: Int) : UserResult()
    data class DatabaseError(val error: Throwable) : UserResult()
}

fun findUserSealed(id: Int): UserResult {
    // Возвращает UserResult
}
```

**Задание**:

- Реализуйте все 4 варианта
- Напишите код использования для каждого варианта
- Сравните читаемость и удобство
- Укажите, когда какой подход лучше использовать

## Задача 2: Рефакторинг чужого кода

Дан legacy код:

```kotlin
fun processPayment(orderId: String): Int {
    try {
        val order = orderRepository.find(orderId)
        if (order == null) {
            return -1  // not found
        }

        val amount = order.amount
        if (amount == null || amount <= 0) {
            return -2  // invalid amount
        }

        val payment = paymentService.charge(amount)
        if (payment == null) {
            return -3  // payment failed
        }

        return 0  // success
    } catch (e: Exception) {
        return -99  // unknown error
    }
}
```

**Задание**:

- Перепишите код используя Result<PaymentResult>
- Создайте sealed class для всех возможных исходов
- Уберите magic numbers
- Добавьте информативные сообщения об ошибках
- Покажите пример использования новой версии

## Задача 3: Проектирование API

Вы проектируете API для библиотеки работы с файлами. Для каждой операции выберите подход к обработке ошибок:

```kotlin
// A. Проверка существования файла
fun fileExists(path: String): ??? {
    // Что вернуть?
}

// B. Чтение содержимого файла
fun readFile(path: String): ??? {
    // Что вернуть?
}

// C. Запись в файл
fun writeFile(path: String, content: String): ??? {
    // Что вернуть?
}

// D. Копирование файла
fun copyFile(source: String, dest: String): ??? {
    // Что вернуть?
}

// E. Получение размера файла
fun getFileSize(path: String): ??? {
    // Что вернуть?
}
```

**Задание**:

- Для каждой функции выберите тип возвращаемого значения
- Обоснуйте выбор между: Boolean, Unit, T?, Result<T>, sealed class
- Реализуйте 2-3 функции с выбранным подходом
- Напишите примеры использования
- Создайте общий summary: в каких случаях какой подход использовать
