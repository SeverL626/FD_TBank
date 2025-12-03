# Домашнее задание 7: Result и runCatching

## Задача 1: Базовое использование runCatching

Напишите функцию для безопасного парсинга JSON:

```kotlin
fun parseUser(json: String): Result<User> {
    // Используйте runCatching для парсинга
    // Верните Result<User>
}

// Используйте функцию:
fun displayUser(json: String) {
    // 1. Вызовите parseUser
    // 2. При успехе - выведите данные пользователя
    // 3. При ошибке - выведите сообщение об ошибке
    // Используйте onSuccess и onFailure
}
```

## Задача 2: Цепочка операций

Реализуйте обработку заказа с несколькими шагами:

```kotlin
// Каждая функция может упасть с ошибкой
fun validateOrder(order: Order): Result<Order> = runCatching {
        require(order.items.isNotEmpty()) { "Order is empty" }
        order
    }

fun calculatePrice(order: Order): Result<Double> = runCatching {
    // Вычисление цены
}

fun applyDiscount(price: Double): Result<Double> = runCatching {
    // Применение скидки
}

fun processPayment(amount: Double): Result<Receipt> = runCatching {
    // Обработка платежа
}
```

**Задание**:

- Напишите функцию `processOrderPipeline(order: Order): Result<Receipt>`
- Используйте mapCatching для цепочки вызовов
- Обработайте ошибки на каждом этапе
- Добавьте логирование ошибок

## Задача 3: Преобразование ошибок

Дана функция:

```kotlin
fun downloadFile(url: String): Result<ByteArray> = runCatching {
    // Скачивание файла
    URL(url).readBytes()
}
```

**Задание**:

- Напишите функцию `downloadAndSave(url: String, path: String): Result<File>`
- Используйте downloadFile и добавьте сохранение в файл
- Если размер файла > 10MB, верните ошибку через recover
- Используйте fold для финальной обработки результата

```kotlin
fun downloadAndSave(url: String, path: String): Result<File> {
    return downloadFile(url)
        .mapCatching { bytes ->
            // Проверка размера и сохранение
        }
        .recover { error ->
            // Обработка ошибок
        }
}
```
